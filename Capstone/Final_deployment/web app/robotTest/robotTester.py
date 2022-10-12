from selenium import webdriver
import time
from random import randint

browser = webdriver.Chrome()
Normal_Robot = {'first_name': 'Normal','last_name': 'Robot' , 'email': "ubot0001@uni.sydney.edu.au", "password": "ubot0001","phone_num": "0424973982"}

browser.get(" http://127.0.0.1:5000/")
assert 'setdose' in browser.title

def register(acc):
    time.sleep(1)
    sign_up = browser.find_element_by_link_text("Sign Up")
    sign_up.click()
    time.sleep(1)
    first_name = browser.find_element_by_name('firstname')
    last_name = browser.find_element_by_name('lastname')
    email = browser.find_element_by_name('email')
    password = browser.find_element_by_name('password')
    phone_num = browser.find_element_by_name('phonenumber')
    first_name.send_keys(acc.get('first_name'))
    time.sleep(1)
    last_name.send_keys(acc.get('last_name'))
    time.sleep(1)
    email.send_keys(acc.get('email'))
    time.sleep(1)
    password.send_keys(acc.get('password'))
    time.sleep(1)
    phone_num.send_keys(acc.get('phone_num'))
    time.sleep(1)
    button = browser.find_element_by_tag_name("button")
    if(button.text == "Sign Up"):
        button.click()

def upload_file(filepath):
    file = browser.find_element_by_name("test_file")
    file.send_keys(filepath)
    time.sleep(1)
    upload_field = browser.find_element_by_xpath("//input[@type='submit']")
    upload_field.click()
    time.sleep(1)

def profile():
    profile = browser.find_element_by_link_text("Profile")
    profile.click()
    time.sleep(1)
    edit = browser.find_element_by_xpath("//input[@value = 'Edit Profile']")
    edit.click()
    time.sleep(1)
    age = browser.find_element_by_name("age")
    age.send_keys("18")
    time.sleep(1)
    gender = browser.find_element_by_name("gender")
    gender.send_keys("male")
    time.sleep(1)
    submit = browser.find_element_by_xpath("//input[@type='submit']")
    submit.click()

def log_out():
    log_out = browser.find_element_by_link_text("Log out")
    log_out.click()

def log_in(acc):
    email = browser.find_element_by_name("email")
    pwd = browser.find_element_by_name("password")
    email.send_keys(acc.get("email"))
    time.sleep(1)
    pwd.send_keys(acc.get("password"))
    time.sleep(1)
    button = browser.find_element_by_tag_name("button")
    if(button.text == "Login"):
        button.click()
    time.sleep(1)

if __name__ == "__main__":
    #register(Normal_Robot)
    log_in(Normal_Robot)
    #upload_file(filepath="E:/comp3888/comp3888_t15_06_group04/static/images/tmp.jpg")
    profile()
    log_out()
    log_in(Normal_Robot)
