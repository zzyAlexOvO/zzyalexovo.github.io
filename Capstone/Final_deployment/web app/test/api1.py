#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import requests
from requests.auth import HTTPBasicAuth
from requests.auth import HTTPDigestAuth

def loginPage():
    
    
    url = 'http://127.0.0.1:5000/login'
    resp=requests.get(url, auth=HTTPDigestAuth('rob@gmail.com', 'qwaszx'))
    code=resp.status_code
    
    if code==200:
        print('/login', "pass")


loginPage()

resp =requests.get("http://127.0.0.1:5000/")

def uploadPage():
    interface_url = 'http://127.0.0.1:5000/upload'
    response_get = requests.get(interface_url)
    
    response_get_code = response_get.status_code
    if response_get_code==200:
        print('/upload', "pass")


uploadPage()


def registerPage():
    interface_url = 'http://127.0.0.1:5000/register'
    response_get = requests.get(interface_url)
    response_get_code = response_get.status_code
    if response_get_code==200:
        print('/register', "pass")


registerPage()

def profile_editPage():
    interface_url = 'http://127.0.0.1:5000/profile_edit'
    response_get = requests.get(interface_url)
    response_get_code = response_get.status_code
    if response_get_code==200:
        print('/profile_edit', "pass")


profile_editPage()

def profilesPage():
    interface_url = 'http://127.0.0.1:5000/profiles'
    response_get = requests.get(interface_url)
    response_get_code = response_get.status_code
    
    if response_get_code==200:
        print('/profiles', "pass")


profilesPage()


def history_idPage():
    
  
    # response = requests.get('http://127.0.0.1:5000/history/17/ user, ',
    # auth = HTTPBasicAuth('rob@gmail.com', 'qwaszx'))
    interface_url = 'http://127.0.0.1:5000/profiles'
    response_get = requests.get(interface_url)
    response_get_code = response_get.status_code
    
    if response_get_code==200:
        print('//history/<history_id>', "pass")


history_idPage()

def adminPage():
    interface_url = 'http://127.0.0.1:5000/admin'
    response_get = requests.get(interface_url)
    response_get_code = response_get.status_code
    if response_get_code==200:
        print('/admin', "pass")


adminPage()
