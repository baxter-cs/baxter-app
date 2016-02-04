from flask import Flask, request, jsonify
from flask.ext.sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///database.db'
db = SQLAlchemy(app)


class Task(db.Model):
    mID = db.Column(db.Integer, primary_key=True, unique=True)
    mTask = db.Column(db.String())
    mDescription = db.Column(db.String())
    mAssignee = db.Column(db.String())
    mDueDate = db.column(db.String())
    mTaskStatus = db.Column(db.String())

    def __init__(self, mTask, mDescription, mAssignee, mDueDate, mTaskStatus):
        self.mTask = mTask
        self.mDescription = mDescription
        self.mAssignee = mAssignee
        self.mDueDate = mDueDate
        self.mTaskStatus = mTaskStatus

    def __repr__(self):
        return '<Task %r>' % self.mID


@app.route('/newTask', methods=['POST'])
def app_task():
    mTask = request.form['mTask']
    mDescription = request.form['mDescription']
    mAssignee = request.form['mAssignee']
    mDueDate = request.form['mDueDate']
    print(mTask + " " + mDescription + " " + mAssignee + " " + mDueDate)
    db.session.add(Task(mTask, mDescription, mAssignee, mDueDate, "To Do"))
    db.session.commit()
    return "Added The Task!"


@app.route('/upgradeTask', methods=['POST'])
def upgrade_task():
    mID = request.form['mID']
    print(mID)
    task = Task.query.filter(Task.mID == mID).first()
    if task.mTaskStatus == "To Do":
        task.mTaskStatus = "In Process"
    elif task.mTaskStatus == "In Process":
        task.mTaskStatus = "Done"
    elif task.mTaskStatus == "Done":
        task.mTaskStatus = "To Do"
    db.session.commit()
    return "Successfully upgraded task"


@app.route('/getTasks')
def get_tasks():
    response = dict()
    response['content'] = "farm"
    return jsonify(**response)


app.run(debug=True, host='0.0.0.0', port=8000)