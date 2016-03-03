from datetime import datetime, timedelta

import bcrypt
import re
import uuid
from flask import Flask, request, jsonify, render_template, make_response, current_app, Response
from flask.ext.sqlalchemy import SQLAlchemy
from functools import update_wrapper

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///bauth.db'
db = SQLAlchemy(app)

# Tables


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

# Functions


def db_create_user(username, password):
    session_id = str(uuid.uuid4())
    current_time = str(datetime.utcnow())
    salt = bcrypt.gensalt()
    ciphered_password = bcrypt.hashpw(str.encode(password), salt)
    db.session.add(User(username, ciphered_password, session_id, current_time))
    db.session.commit()
    return "Successfully added a new user"


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

# Routes


@app.route('/login', methods=['POST', 'OPTIONS'])
def login():
    data = request.json
    response = {}
    try:
        username = data.get('username')
        password = data.get('password')
        uuid = make_session(username, password)
        if uuid == "Invalid Password":
            response["status"] = "error"
            response["response"] = "invalid password"
            response["data"] = "invalid"
        elif uuid == "Username Doesn't Exist":
            response["status"] = "error"
            response["response"] = "username doesn't exist"
            response["data"] = "invalid"
        elif uuid == "invalid":
            response["status"] = "error"
            response["response"] = "missing"
            response["data"] = "invalid"
        else:
            response["data"] = uuid
            response["response"] = "logged in"
            response["status"] = "success"
    except:
        response["data"] = "invalid"
        response["response"] = "log in failed"
        response["status"] = "error"
    return_response = make_response(jsonify(**response))
    return_response.headers['Access-Control-Allow-Origin'] = '*'
    return_response.headers['Access-Control-Allow-Headers'] = "Content-Type, Access-Control-Allow-Origin"
    return return_response


@app.route('/verifyLogin', methods=['POST'])
def r_verify_login():
    data = request.json
    response = {}
    try:
        uuid = data.get('uuid')
        if check_if_valid_session(uuid):
            response["response"] = "valid uuid"
        else:
            response["response"] = "invalid uuid"
    except:
        response["response"] = "missing"

    return_response = make_response(jsonify(**response))
    return_response.headers['Access-Control-Allow-Origin'] = '*'
    return_response.headers['Access-Control-Allow-Headers'] = "Content-Type, Access-Control-Allow-Origin"
    return return_response


@app.route('/signUp', methods=['POST'])
def sign_up():
    data = request.json
    response = {}
    try:
        username = data.get('username')
        password = data.get('password')
        email = data.get('email')
        regex_pattern = re.compile("[^\w']")
        if regex_pattern.sub(' ', username) == username and regex_pattern.sub(' ', password) == password:
            pre_existing_user = User.query.filter(User.username == username).first()
            if pre_existing_user is None:
                db_create_user(username, password)
                response["response"] = "success"
            else:
                response["response"] = "username taken"
        else:
            response["response"] = "invalid"
    except:
        response["response"] = "missing"

    return_response = make_response(jsonify(**response))
    return_response.headers['Access-Control-Allow-Origin'] = '*'
    return_response.headers['Access-Control-Allow-Headers'] = "Content-Type, Access-Control-Allow-Origin"
    return return_response




@app.route('/')
def index():
    return "Welcome to BAuth"


app.run(debug=True, host='0.0.0.0', port=1754)  # 1754 is ba in Base 64
