from datetime import datetime

import bcrypt
import re
import uuid
from flask import Flask, request, jsonify, render_template
from flask.ext.sqlalchemy import SQLAlchemy

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


class TeamMember(db.Model):
    id = db.Column(db.Integer, primary_key=True, unique=True)
    user = db.Column(db.String())
    team = db.Column(db.String())

    def __init__(self, user, team):
        self.user = user
        self.team = team

    def __repr__(self):
        return '<TeamMember %r>' % self.id


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True, unique=True)
    username = db.Column(db.String(), unique=True)
    password = db.Column(db.String())
    session_id = db.Column(db.String())
    session_time = db.Column(db.String())

    def __init__(self, username, password, session_id, session_time):
        self.username = username
        self.password = password
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
    db.session.add(User(username, ciphered_password, session_id, current_time))
    db.session.commit()
    return "Successfully added a new user"


def db_create_team_member(user, team):
    db.session.add(TeamMember(user, team))
    db.session.commit()


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


def make_session(iUsername, iPassword):
    print("input: " + iUsername + " & " + iPassword)
    try:
        account = User.query.filter(User.username == iUsername).first()
        if account is not None:
            print("Username matched")
            stored_password = account.password
            print("Stored password: {}".format(stored_password))
            if bcrypt.hashpw(bytes(iPassword, 'utf-8'), stored_password) == stored_password:
                print("Stored Password and Hashed password Match")
                return account.session_id
            else:
                print("Stored Password and Hashed password don't match")
                return "Invalid Password"
        else:
            print("Username Doesn't Exist")
            return "Username Doesn't Exist"
    except Exception:
        print(Exception.__dict__)
        print("returning invalid")
        return "invalid"


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
def delete_tank():
    data = request.json
    response = {}

    try:
        mID = data.get('mID')
        Task.query.filter(Task.mID == mID).delete()
        db.session.commit()
        response["response"] = "deleted task"
    except:
        response["response"] = "failed at deleting task"
    return jsonify(**response)


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


@app.route('/verifyLogin', methods=['POST'])
def verify_login():
    data = request.json
    response = dict()
    print(data)
    try:
        uuid = data.get('uuid')
        if check_if_valid_session(uuid):
            response["response"] = "valid uuid"
            return jsonify(**response)
        else:
            response["response"] = "invalid uuid"
            return jsonify(**response)
    except:
        response["response"] = "invalid entered uuid"
        return jsonify(**response)


@app.route('/login', methods=['POST'])
def login():
    data = request.json
    response = dict()
    try:
        username = data.get('username')
        password = data.get('password')
        response["data"] = make_session(username, password)
        response["response"] = "logged in"
    except:
        response["data"] = "invalid"
        response["response"] = "log in failed"
    return jsonify(**response)


@app.route('/debugUserCreate/<username>/<password>')
def debug_user_create(username, password):
    db_create_user(username, password)
    return "New Account Created!\n Username: {}\n Password: {}".format(username, password)


@app.route('/signUp', methods=['POST'])
def sign_up():
    data = request.json
    try:
        username = data.get('username')
        password = data.get('password')
        email = data.get('email')
    except:
        return "error"

    regex_pattern = re.compile("[^\w']")

    if regex_pattern.sub(' ', username) != username or regex_pattern.sub(' ', password) != password:
        return "invalid"

    if username is None or password is None or email is None:
        return "missing"

    pre_existing_user = User.query.filter(User.username == username).first()
    if pre_existing_user is not None:
        return "usernameTaken"

    db_create_user(username, password)
    return "success"


@app.route('/refreshTask', methods=['POST'])
def refresh_task():
    data = request.json
    response = {}
    try:
        uuid = data.get('uuid')
        mID = data.get('mID')
    except:
        response['status'] = "missing"
        return jsonify(**response)

    try:
        task = Task.query.filter(Task.mID == mID).first()
        tasks = {'mTask': task.mTask,
                    'mDescription': task.mDescription,
                    'mAssignee': task.mAssignee,
                    'mDueDate': task.mDueDate,
                    'mID': task.mID,
                    'mTaskStatus': task.mTaskStatus
                    }
        response['tasks'] = tasks
        response['status'] = "success"
    except:
        response['status'] = "error"
        return jsonify(**response)

    return jsonify(**response)


@app.route('/updateTask', methods=['POST'])
def update_task():
    data = request.json

    try:
        mId = data.get('mID')
        mDescription = data.get('mDescription')
        mTitle = data.get('mTask')
        mAssignee = data.get('mAssignee')
        uuid = data.get('uuid')
        mDueDate = data.get('mDueDate')
    except:
        return "missing"

    try:
        task = Task.query.filter(Task.mID == mId).first()
        task.mDescription = mDescription
        task.mTask = mTitle
        task.mAssignee = mAssignee
        task.mDueDate = mDueDate
        db.session.commit()
    except:
        return "error"

    return "success"


@app.route('/joinTeam', methods=['POST'])
def join_team():
    data = request.json

    try:
        tID = data.get('tID')
        uuid = data.get('uuid')
    except:
        return "missing"

    try:
        user = User.query.filter(User.session_id == uuid).first()
        team = User.query.filter(Team.id == tID).first()
    except:
        return "error"

    db_create_team_member(user.session_id, team.id)

    return "success"


@app.route('/')
def testing_website():
    return render_template('index.html')


app.run(debug=True, host='0.0.0.0', port=7999)
