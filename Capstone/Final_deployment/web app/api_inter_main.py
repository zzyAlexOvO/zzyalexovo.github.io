#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import requests
import os
import json
import cv2
import base64
from  werkzeug.datastructures import ImmutableMultiDict,FileStorage

register_url = 'http://127.0.0.1:5000/register'
login_url = 'http://127.0.0.1:5000/login'
upload_url = 'http://127.0.0.1:5000/upload'
profile_edit_url = "http://127.0.0.1:5000/profile_edit"
profiles_url = "http://127.0.0.1:5000/profiles"
history_detail_url = "http://127.0.0.1:5000/history/"
admin_url = "http://127.0.0.1:5000/admin"

def send_post(url,data):
    res=requests.post(url=url,data=data)
    return res

def send_get(url,data):
    res=requests.get(url=url,data=data)
    return res

def register_page():
    get_data={
        'data':'get_register_page'
    }
    methods = ['GET','POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(register_url, get_data)
            print('registerGETstatus:%s'%(result))
        else:
            for i in range(11):
                firstname = str(i)*9
                lastname = str(i)*9
                email = str(i)*9+"@qq.com"
                phonenumber = str(i)*9
                password = str(i)*9

                post_data = {
                    'data': 'post_register_page',
                    'firstname': firstname,
                    'lastname': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password
                }
                result = send_post(register_url, post_data)
                print('registerPOST NO.%sregister status:%s'%(i,result))
        results.append({method:result})
    return results

def login_page():
    get_data = {
        'data': 'get_login_page'
    }


    methods = ['GET', 'POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(login_url, get_data)
            print('loginGETstatus:%s' % (result))
        else:
            for i in range(11):
                firstname = str(i) * 8
                lastname = str(i) * 8
                email = str(i) * 8
                phonenumber = str(i) * 8
                password = str(i) * 8

                post_data = {
                    'data': 'post_register_page',
                    'firstname': firstname,
                    'lastname': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password
                }
                result = send_post(login_url, post_data)
                print('loginPOST No%sloginstatus:%s' % (i, result))
        results.append({method: result})
    return results

def upload_page():
    get_data = {
        'data': 'get_login_page'
    }
    methods = ['GET','POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(upload_url, get_data)
            print('testingupload GET')
        else:
            for i in range(11):
                firstname = str(i) * 8
                lastname = str(i) * 8
                email = str(i) * 8
                phonenumber = str(i) * 8
                password = str(i) * 8

                post_data = {
                    'data': 'post_register_page',
                    'firstname': firstname,
                    'lastname': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password
                }
                login_result = send_post(login_url, post_data)
                print('UPLAOD-login POST No%slogin status:%s' % (i, login_result))

                file = {'test_file': ('1.png', open("/Users/zhiranbai/Desktop/main_code_test/static/upload_images/20190906_094830.jpg", 'rb'), 'image/png')}
                result = requests.post(url=upload_url,files=file)
        results.append({method: result})
    return results

def profile_edit_page():
    get_data = {
        'data': 'profile_edit_page'
    }


    methods = ['GET', 'POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(profile_edit_url, get_data)
            print('profile_edit GETstatus:%s' % (result))
        else:
            for i in range(11):
                profile_pic,gender,age,address,occupation,current_password,new_password,confirm_password = \
                    str(i) * 8,'male',str(i),str(i) * 8,str(i) * 8,str(i) * 8,str(i) * 8,str(i) * 8

                firstname = str(i) * 8
                lastname = str(i) * 8
                email = str(i) * 8 + "@qq.com"
                phonenumber = str(i) * 8
                password = str(i) * 8

                post_data = {
                    'data': 'profile_edit_page',
                    'profile_pic': profile_pic,
                    'gender': gender,
                    'age': age,
                    'address': address,
                    'occupation': occupation,
                    'current_password': current_password,
                    'new_password': new_password,
                    'confirm_password': confirm_password,

                    'first_name': firstname,
                    'last_name': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password
                }
                result = send_post(login_url, post_data)
                print('UPLAODloginPOST No%slogin status:%s' % (i, result))

                result = send_post(profile_edit_url, post_data)
                print('profile_edit POST No%s:%s' % (i, result))
        results.append({method: result})
    return results


def profiles_page():
    get_data = {
        'data': 'profiles_page'
    }


    methods = ['GET', 'POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(profiles_url, get_data)
            print('profile_edit GET status:%s' % (result))
        else:
            for i in range(11):
                firstname = str(i) * 8
                lastname = str(i) * 8
                email = str(i) * 8 + "@qq.com"
                phonenumber = str(i) * 8
                password = str(i) * 8
                delete_id = 21

                post_data = {
                    'data': 'profiles_page',
                    'first_name': firstname,
                    'last_name': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password,
                    'delete_id': delete_id
                }
                result = send_post(login_url, post_data)
                print('PROFILES login POST No%slogin status:%s' % (i, result))

                result = send_post(profiles_url, post_data)
                print('PROFILES POST No %s status:%s' % (i, result))
        results.append({method: result})
    return results

def history_detail_page():
    get_data = {
        'data': 'history_detail_page'
    }

    methods = ['GET', 'POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(profiles_url, get_data)
            print('profile_edit GET s status:%s' % (result))
        else:
            for i in range(11):
                firstname = str(i) * 8
                lastname = str(i) * 8
                email = str(i) * 8 + "@qq.com"
                phonenumber = str(i) * 8
                password = str(i) * 8

                post_data = {
                    'data': 'history_detail_page',
                    'first_name': firstname,
                    'last_name': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password
                }
                result = send_post(login_url, post_data)
                print('PROFILES loginpage POST No%s次loginstatus:%s' % (i, result))

                for j in range(100):
                    history_detail_url_test = history_detail_url+str(j)
                    result = send_post(history_detail_url_test, post_data)
                    print('PROFILES POST No%s %sNo of history status:%s' % (i,j, result))
        results.append({method: result})
    return results


def admin_page():
    get_data = {
        'data': 'admin_page'
    }

    methods = ['GET', 'POST']
    results = []
    for method in methods:
        result = None
        if method == 'GET':
            result = send_get(admin_url, get_data)
            print('admin_page GET status:%s' % (result))
        else:
            for i in range(11):
                firstname = str(i) * 8
                lastname = str(i) * 8
                email = str(i) * 8 + "@qq.com"
                phonenumber = str(i) * 8
                password = str(i) * 8
                delete_ids = [0,10,21,25]
                post_data = {
                    'data': 'admin_page',
                    'first_name': firstname,
                    'last_name': lastname,
                    'email': email,
                    'phonenumber': phonenumber,
                    'password': password,
                }
                result = send_post(login_url, post_data)
                print('admin_page login POST No%s login status:%s' % (i, result))

                for delete_id in delete_ids:
                    post_data['delete_id'] = delete_id
                    result = send_post(admin_url, post_data)
                    print('admin_page POST No%s次的delete_id:%s status:%s' % (i,delete_id, result))
        results.append({method: result})
    return results


if __name__ == '__main__':


    results = register_page()
    print({'register test':results})
    print('*' * 50)

    results = login_page()
    print({'login test': results})
    print('*' * 50)

    results = upload_page()
    print({'upload test': results})
    print('*' * 50)

    results = profile_edit_page()
    print({'PROFILE edit test': results})
    print('*' * 50)

    results = profiles_page()
    print({'PROFILE test': results})
    print('*' * 50)

    results = history_detail_page()
    print({'HISTORY test': results})
    print('*' * 50)

    results = admin_page()
    print({'ADMIN_PAGE test': results})
    print('*' * 50)
