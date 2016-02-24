from datetime import datetime
from flask import Flask, request, jsonify
from flask.ext.sqlalchemy import SQLAlchemy
import bcrypt, uuid

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///database.db'
db = SQLAlchemy(app)


# Tables


class Task(db.Model):
    mID = db.Column(db.Integer, primary_key=True, unique=True)
    mTask = db.Column(db.String())
    mDescription = db.Column(db.String())
    mAssignee = db.Column(db.String())
    mDueDate = db.Column(db.String())
    mTaskStatus = db.Column(db.String())

    def __init__(self, mTask, mDescription, mAssignee, mDueDate, mTaskStatus):
        self.mTask = mTask
        self.mDescription = mDescription
        self.mAssignee = mAssignee
        self.mDueDate = mDueDate
        self.mTaskStatus = mTaskStatus

    def __repr__(self):
        return '<Task %r>' % self.mID


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True, unique=True)
    username = db.Column(db.String(), unique=True)
    password = db.Column(db.String())
    salt = db.Column(db.String())
    session_id = db.Column(db.String())
    session_time = db.Column(db.String())

    def __init__(self, username, password, salt, session_id, session_time):
        self.username = username
        self.password = password
        self.salt = salt
        self.session_id = session_id
        self.session_time = session_time

    def __repr__(self):
        return '<User %r>' % self.username


class Team(db.Model):
    id = db.Column(db.Integer, primary_key=True, unique=True)
    name = db.Column(db.String(), unique=True)
    owner = db.Column(db.String())

    def __init__(self, name, owner):
        self.name = name
        self.owner = owner

    def __repr__(self):
        return '<Team %r>' % self.name


# Methods


def db_create_user(username, password):
    session_id = str(uuid.uuid4())
    current_time = str(datetime.utcnow())
    salt = bcrypt.gensalt()
    ciphered_password = bcrypt.hashpw(str.encode(password), salt)
    db.session.add(User(username, ciphered_password, salt, session_id, current_time))
    db.session.commit()
    return "Successfully added a new user"


def check_if_valid_session(session_id):
    if session_id == "invalid":
        return False
    else:
        try:
            claimsTobe = User.query.filter(User.session_id == session_id).first()
            if claimsTobe is not None:
                return True
        except:
            return False


def make_session(username, inputtedPassword):
    try:
        account = User.query.filter(User.username == username).first()
        hashed_password = account.password
        salt = account.salt
        password = bcrypt.hashpw(str.encode(inputtedPassword, salt))
        if hashed_password == password:
            return account.session_id
        else:
            return False
    except:
        return False


# Routes


@app.route('/newTask', methods=['POST'])
def app_task():
    data = request.json
    mTask = data.get('mTask')
    mDescription = data.get('mDescription')
    mAssignee = data.get('mAssignee')
    mDueDate = data.get('mDueDate')
    print(mTask + " " + mDescription + " " + mAssignee + " " + mDueDate)
    db.session.add(Task(mTask, mDescription, mAssignee, mDueDate, "To Do"))
    db.session.commit()
    return "Added The Task!"


@app.route('/upgradeTask', methods=['POST'])
def upgrade_task():
    data = request.json
    mID = data.get('mID')
    print(mID)
    task = Task.query.filter(Task.mID == mID).first()
    if task.mTaskStatus == "To Do":
        task.mTaskStatus = "In Process"
    elif task.mTaskStatus == "In Process":
        task.mTaskStatus = "Done"
    db.session.commit()
    return "Successfully upgraded task"


@app.route('/getTasks')
def get_tasks():
    isInitTodo = False
    isInitDoing = False
    isInitDone = False
    tasks = []
    for task in Task.query.all():
        if task.mTaskStatus == "To Do":
            isInitTodo = True
        elif task.mTaskStatus == "In Process":
            isInitDoing = True
        elif task.mTaskStatus == "Done":
            isInitDone = True
        response = {'mTask': task.mTask,
                    'mDescription': task.mDescription,
                    'mAssignee': task.mAssignee,
                    'mDueDate': task.mDueDate,
                    'mID': task.mID,
                    'mTaskStatus': task.mTaskStatus
                    }
        tasks.append(response)
    response = {}
    response['tasks'] = tasks
    response['meta'] = {}
    response['meta']['isInitTodo'] = isInitTodo
    response['meta']['isInitDoing'] = isInitDoing
    response['meta']['isInitDone'] = isInitDone
    return jsonify(**response)


@app.route('/deleteTask', methods=['POST'])
def delete_task():
    mID = request.form['mID']
    print(mID)
    Task.query.filter(Task.mID == mID).delete()
    db.session.commit()
    return "Deleted Task"


@app.route('/test', methods=['POST'])
def test():
    app.logger.debug("JSON received...")
    app.logger.debug(request.json)

    if request.json:
        data = request.json
        print(data.get('mTask'))
        return "Thanks. Your task is %s" % data.get("mTask")

    else:
        return "no json received"


app.run(debug=True, host='0.0.0.0', port=8000)
