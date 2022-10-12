from flask import *
import uuid
from werkzeug.utils import secure_filename
import hashlib
import os
import cv2
import time
import torch.nn as nn
from datetime import timedelta
import recognize.recognize
import recognize.pytorch_recog
import recognize.tensflow_recog
import torch.nn.functional as F
import redis_save
import functions
import base64
from flask_docs import ApiDoc

from flask import Flask, request, render_template
from flask_sqlalchemy import SQLAlchemy
# import query
# import sqlalchemy 

Allowed_ext = {'png', 'jpg', 'JPG', 'PNG', 'tmp'}
redis_config = {
    "url": "localhost",
    "port": 6379
}
cur_email = None

app = Flask(__name__)
ApiDoc(
    app,
    title="Sample App",
    version="1.0.0",
    description="A simple app API",
)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///database/userdata.db'
# app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN'] = True
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
# app._static_folder = "./templates/static"
db = SQLAlchemy()
db.init_app(app)


class History(db.Model):
    __tablename__ = 'History'

    HistoryID = db.Column(db.Integer, primary_key=True)
    UserID = db.Column(db.Integer)
    Date = db.Column(db.Date, nullable=False)
    Time = db.Column(db.Time, nullable=False)
    Volume = db.Column(db.Numeric(20, 2), nullable=False)
    Medicine = db.Column(db.String(50), nullable=False)
    Comments = db.Column(db.String(300))
    ImagePath = db.Column(db.String(50), nullable=False)


class User(db.Model):
    __tablename__ = 'User'

    UserID = db.Column(db.Integer, primary_key=True)
    Username = db.Column(db.String(30), nullable=False)
    Hash = db.Column(db.String(200), nullable=False)
    Email = db.Column(db.String(30), nullable=False)
    Address = db.Column(db.String(50))
    Phone = db.Column(db.String(30), nullable=False)
    Age = db.Column(db.Integer)
    Gender = db.Column(db.String(10))
    Status = db.Column(db.String(20), nullable=False)
    Occupation = db.Column(db.String(20), nullable=False)
    Admin = db.Column(db.Integer)


with app.app_context():
    db.create_all()


class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.conv1 = nn.Conv2d(in_channels=3, out_channels=6, kernel_size=5)
        self.pool1 = nn.MaxPool2d(kernel_size=2, stride=2)
        self.conv2 = nn.Conv2d(in_channels=6, out_channels=16, kernel_size=5)
        self.fc1 = nn.Linear(in_features=16 * 72 * 197, out_features=120)
        self.fc2 = nn.Linear(in_features=120, out_features=84)
        self.fc3 = nn.Linear(in_features=84, out_features=10)

    def forward(self, x):
        # Max pooling over a (2, 2) window
        x = F.max_pool2d(F.relu(self.conv1(x)), (2, 2))
        # If the size is a square you can only specify a single number
        x = F.max_pool2d(F.relu(self.conv2(x)), 2)
        #         print(x.shape)

        x = x.view(-1, self.num_flat_features(x))
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = self.fc3(x)
        return x

    def num_flat_features(self, x):
        size = x.size()[1:]  # all dimensions except the batch dimension
        num_features = 1
        for s in size:
            num_features *= s
        return num_features


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in Allowed_ext


app.send_file_max_age_default = timedelta(seconds=1)


@app.route('/upload', methods=['POST', 'GET'])
def upload():
    user_email = cur_email
    if request.method == 'POST':
        print(request.files)
        f = request.files['test_file']
        if not (f and allowed_file(f.filename)):
            return jsonify({"error": 1001, "msg": "check the type of image"})
        basepath = os.path.dirname(__file__)
        upload_path = os.path.join(basepath, "static/images", secure_filename(f.filename))
        f.save(upload_path)
        # redis_save.store_image(cur_email,)
        tmp_path = os.path.join(basepath, "static/images", "tmp.jpg")

        # choose image & recognize column
        volume, cv_madicine = recognize.recognize.image_recoginition(upload_path)
        # py_madicine = recognize.pytorch_recog.image6_recog(upload_path)
        # tens_madicine = recognize.tensflow_recog.recognize(upload_path)
        # result = [tens_madicine, cv_madicine]

        result = []

        validate_dict = {}
        # result = [tens_madicine, cv_madicine, py_madicine]
        for i in range(len(result)):
            if result[i] not in validate_dict:
                validate_dict[result[i]] = 1
            else:
                validate_dict[result[i]] = validate_dict[result[i]] + 1

        img = cv2.imread(upload_path)
        cv2.imwrite(tmp_path, img)
        if volume < 0:
            volume = abs(volume)

        # store the images for history function
        user = User.query.filter_by(Email=user_email).first()
        user_id = user.UserID
        cur_time = time.strftime("%H:%M:%S", time.localtime())
        date = time.strftime("%Y-%m-%d", time.localtime())
        history_id = functions.add_history(user_id, date, cur_time, round(volume, 2), cv_madicine, str(upload_path))
        # new_path = os.path.join(basepath, "static/images", history_id, ".jpg")

        # img_stream = base64.b64encode(f.stream.read())
        # print(img_stream)
        with open(tmp_path, 'rb') as img_f:
            img_stream = img_f.read()
            img_stream = base64.b64encode(img_stream)
        # redis_save.store_image(user_id, history_id, "", open(upload_path, "rb").read(), redis_config["url"],redis_config["port"])

        image_path = str(upload_path).replace("static", ".")
        return render_template('result.html', image_path=image_path, val1=cur_time, volumn=round(volume, 2),
                               madicine=cv_madicine, img_stream=img_stream)
    return render_template("recog.html")
    


@app.route('/login', methods=['POST', 'GET'])
def page_login():
    if request.method == "GET":
        return render_template('login_setdoes.html')
    elif request.method == "POST":
        email = request.form.get('email')
        password = request.form.get('password')
        hash_code = hashlib.sha256(password.encode()).hexdigest()
        login_check_result = functions.login_check(email, hash_code)
        if login_check_result == -1:
            status_code = Response(status=400)
            return '''<style>
                    p {text-align: center;}
                        form {text-align: center;}
                        </style>
                <p>The combination of Email and password is incorrect.</p>
                <p><a href="/login">go back to login page</a></p>''' ,status_code
	
        global cur_email
        cur_email = email
        if (email == 'Admin001@gmail.com') and (password == 'Admin001'):
            return redirect('/admin')
        return redirect('/upload')


@app.route('/register', methods=['POST', 'GET'])
def page_register():
    if request.method == 'GET':
        return render_template('register_page.html')
    elif request.method == 'POST':
        username = request.form.get('firstname') + " " + request.form.get('lastname')
        email = request.form.get('email')
        phone = request.form.get('phonenumber')
        password = request.form.get('password')
        # userID = uuid.uuid4().int
        status = 'normal'
        admin = 0
        occupation = 'Normal user'
        hash_code = hashlib.sha256(password.encode()).hexdigest()
        check_result = functions.register_user(username, email, phone, hash_code, status, admin, occupation)
        if check_result == -1:
            status_code = Response(status=400)
            return '''<style>
                    p {text-align: center;}
                            form {text-align: center;}
                            </style>
                <p>This Email has been registered. Please use another Email.</p>
                <p><a href="/register">go back to register page</a></p>''',status_code
        return render_template('login_setdoes.html')


@app.route('/profile_edit', methods=['POST', 'GET'])
def profile_edit():
    if request.method == 'GET':
        return render_template('profile_edit_page.html')
    else:
        profile_pic = request.form.get('profile_pic')
        first_name = request.form.get('first_name')
        last_name = request.form.get('last_name')
        email = request.form.get('email')
        age = request.form.get('age')
        address = request.form.get('address')
        gender = request.form.get('gender')
        occupation = request.form.get('occupation')
        current_password = request.form.get('current_password')
        new_password = request.form.get('new_password')
        confirm_password = request.form.get('confirm_password')
        new_hash = ""
        if (current_password != "" and new_password != "" and confirm_password != ""):
            hash_code = hashlib.sha256(current_password.encode()).hexdigest()
            new_hash = hashlib.sha256(new_password.encode()).hexdigest()
            if str(new_password) != str(confirm_password):
                print("The two password is not the same")
                return "The two password is not the same"
            elif not functions.login_check(email, hash_code):
                print("Entered wrong current password")
                return "Entered wrong current password"

        if functions.update_profile(cur_email, profile_pic, first_name, last_name, email, age, address, gender,
                                    occupation, new_hash):
            user = User.query.filter_by(Email=email).first()
            return redirect('/profiles')
        else:
            print("Update failed for unknown reason")
            return "Update failed for unknown reason"

@app.route('/admin', methods=['POST', 'GET'])
def admin():
    usr_email = cur_email
    user = User.query.filter_by(Email=usr_email).first()

    user_list_1 = User.query.filter_by(Admin=None).all()
    user_list_2 = User.query.filter_by(Admin=0).all()
    user_list = user_list_1 + user_list_2

    if request.method == 'POST':
        delete_id = int(request.form.get('delete_id'))
        delete_user = User.query.filter_by(UserID=delete_id).first()
        if delete_user:
            db.session.delete(delete_user)
            db.session.commit()
        
        user_list_1 = User.query.filter_by(Admin=None).all()
        user_list_2 = User.query.filter_by(Admin=0).all()
        user_list = user_list_1 + user_list_2
        return render_template('admin_page.html', user=user, user_list=user_list)

    return render_template('admin_page.html', user=user, user_list=user_list)


@app.route('/admin_edit/<user_id>', methods=['POST', 'GET'])
def admin_edit(user_id):
    if request.method == 'GET':
        return render_template('admin_edit_page.html')
    elif request.method == 'POST':
        profile_pic = request.form.get('profile_pic')
        first_name = request.form.get('first_name')
        last_name = request.form.get('last_name')
        email = request.form.get('email')
        age = request.form.get('age')
        address = request.form.get('address')
        gender = request.form.get('gender')
        occupation = request.form.get('occupation')
        status = request.form.get('status')
        current_password = request.form.get('current_password')
        new_password = request.form.get('new_password')
        confirm_password = request.form.get('confirm_password')
        new_hash = ""
        if (current_password != "" and new_password != "" and confirm_password != ""):
            hash_code = hashlib.sha256(current_password.encode()).hexdigest()
            new_hash = hashlib.sha256(new_password.encode()).hexdigest()
            if str(new_password) != str(confirm_password):
                print("The two password is not the same")
                return "The two password is not the same"
            elif not functions.login_check(email, hash_code):
                print("Entered wrong current password")
                return "Entered wrong current password"

        edit_user = User.query.filter_by(UserID = user_id).first()
        edit_user_email = edit_user.Email
        if functions.admin_edit_profile(edit_user_email, profile_pic, first_name, last_name, email, age, address, gender,
                                    occupation, new_hash, status):
            edit_user = User.query.filter_by(Email=edit_user_email).first()
            return redirect('/admin')
        else:
            print("Update failed for unknown reason")
            return "Update failed for unknown reason"


@app.route('/profiles', methods=['POST', 'GET'])
def profiles():
    user_email = cur_email
    user = User.query.filter_by(Email=user_email).first()

    history_list = History.query.filter_by(UserID=user.UserID).all()

    for history in history_list:
        print(history.HistoryID)

    if request.method == 'POST':
        delete_id = int(request.form.get('delete_id'))
        delete_history = History.query.filter_by(HistoryID=delete_id).first()
        if delete_history:
            db.session.delete(delete_history)
            db.session.commit()
            history_list = History.query.filter_by(UserID=user.UserID).all()
        return render_template('profile page.html', user=user, history_list=history_list)

    return render_template('profile page.html', user=user, history_list=history_list)


@app.route('/history/<history_id>', methods=['POST', 'GET'])
def history_detail(history_id):
    user_email = cur_email
    user = User.query.filter_by(Email=user_email).first()
    history = History.query.filter_by(HistoryID=history_id).first()
    if history:
        print(history)
        new_imagepath = history.ImagePath.replace("static", ".")
        # print(user.Username)
    return render_template('history.html', user=user, history=history, new_imagepath=new_imagepath)


@app.errorhandler(403)
def forbidden(e):
    return render_template('403.html'), 403

@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404

@app.errorhandler(500)
def page_not_found(e):
    return render_template('500.html'), 500

@app.errorhandler(503)
def service_unavailable(e):
    return render_template('503.html'), 503

@app.errorhandler(405)
def page_not_found(e):
    return render_template('405.html'), 405

@app.errorhandler(408)
def page_not_found(e):
    return render_template('408.html'), 408

@app.route('/')
def hello_world():  # put application's code here
    return render_template("login_setdoes.html")


if __name__ == '__main__':
    app.run(host='127.0.0.1',port=5000)
