import sqlite3

import pymysql


# -----------------------------------------------------------------------------
# register
# -----------------------------------------------------------------------------

def register_user(username, email, phone, hash_code, status, admin, occupation):
    conn = sqlite3.connect('database/userdata.db')
    c = conn.cursor()
    # check whether the Email has been registered
    c.execute("SELECT Email FROM User")
    email_list = [item[0] for item in c.fetchall()]

    if email not in email_list:
        c = conn.execute(
            "INSERT INTO User(Username, Hash, Email, Phone, Status, Admin, Occupation) VALUES (?,?,?,?,?,?,?)",
            (username, hash_code, email, phone, status, admin, occupation)
        )
        conn.commit()
        c.close()
        return True
    else:
        return False


# -----------------------------------------------------------------------------
# Login
# -----------------------------------------------------------------------------
def login_check(email, password):
    conn = sqlite3.connect('database/userdata.db')
    c = conn.cursor()

    c.execute("SELECT 1 FROM User WHERE Email = ? and Hash = ?", (email, password))
    check_data = c.fetchone()

    if not check_data:
        login = False
    else:
        login = True

    if login:
        conn.commit()
        c.close
        return True
    else:
        return False


# -----------------------------------------------------------------------------
# Profile
# -----------------------------------------------------------------------------
def update_profile(cur_email, profile_pic, first_name, last_name, email, age, address, gender, occupation, new_hash):
    username = first_name + " " + last_name
    query = ""
    if (username != " "):
        username = "Username = '" + username + "'"
        query = query + username + ","
    if (email != ""):
        query = query + "Email = '" + email + "',"
    if (age != ""):
        query = query + "Age = '" + age + "',"
    if (address != ""):
        query = query + "Address = '" + address + "',"
    if (gender != ""):
        query = query + "Gender = '" + gender + "',"
    if (occupation != ""):
        query = query + "Occupation = '" + occupation + "',"
    if (new_hash != ""):
        query = query + "Hash = '" + new_hash + "',"
    if (query.strip() == ""):
        return True
    elif (query.endswith(",")):
        query = query[:-1]
    sql = f"""
        UPDATE User
        SET {query}
        WHERE Email = '{cur_email}'
    """.format(cur_email=cur_email, query=query)
    # print(sql)
    conn = sqlite3.connect('database/userdata.db')
    cursor = conn.cursor()
    cursor.execute(sql)
    conn.commit()
    cursor.close()
    return True

def admin_edit_profile(cur_email, profile_pic, first_name, last_name, email, age, address, gender, occupation, new_hash, status):
    username = first_name + " " + last_name
    query = ""
    if (username != " "):
        username = "Username = '" + username + "'"
        query = query + username + ","
    if (email != ""):
        query = query + "Email = '" + email + "',"
    if (age != ""):
        query = query + "Age = '" + age + "',"
    if (address != ""):
        query = query + "Address = '" + address + "',"
    if (gender != ""):
        query = query + "Gender = '" + gender + "',"
    if (occupation != ""):
        query = query + "Occupation = '" + occupation + "',"
    if (status != ""):
        query = query + "Status = '" + status + "',"
    if (new_hash != ""):
        query = query + "Hash = '" + new_hash + "',"
    if (query.strip() == ""):
        return True
    elif (query.endswith(",")):
        query = query[:-1]
    sql = f"""
        UPDATE User
        SET {query}
        WHERE Email = '{cur_email}'
    """.format(cur_email=cur_email, query=query)
    print(sql)
    conn = sqlite3.connect('database/userdata.db')
    cursor = conn.cursor()
    cursor.execute(sql)
    conn.commit()
    cursor.close()
    return True


"""    except Exception:
        print(Exception)
        cursor.close()
        return False"""


def compare_password(hash, userID):
    conn = sqlite3.connect('database/userdata.db')
    correct = conn.cursor(
        f"SELECT Hash FROM user WHERE userID = {userID}".format(userID=userID)
    )
    return hash == correct


# -----------------------------------------------------------------------------
# History
# -----------------------------------------------------------------------------
def add_history(user_id, date, time, volume, medicine_type, image_path):
    conn = sqlite3.connect('database/userdata.db')
    c = conn.cursor()

    c = conn.execute(
        "INSERT INTO History(UserID, Date, Time, Volume, Medicine, ImagePath) VALUES (?,?,?,?,?,?)",
        (user_id, date, time, volume, medicine_type, image_path)
    )

    con = conn.execute(
        "SELECT HistoryID FROM History WHERE UserID=? AND Date=? AND Time=?", (user_id, date, time)
    )
    result = con.fetchone()
    history_id = result[0]

    conn.commit()
    c.close()

    return history_id


def connection():
    db = pymysql.connect(host="172.31.2.4", user="root", password="123", port=3306, charset="utf8")
    cursor = db.connect()
    return cursor, db


def execute(sql, db, cursor):
    cursor.execute(sql)
    db.commit()


def test_connection():
    cursor,db = connection()
    sql = "select * from users;"
    print(cursor.execute(sql))
    cursor.close()
    return True

def test_insert():
    cursor, db = connection()
    sql = "INSERT INTO User(Username, Hash, Email, Phone, Status) VALUES ('test','123456','1@1.com','123321','1')"
    print(cursor.execute(sql))
    cursor.close()
    return True

def test_select():
    cursor, db = connection()
    sql = "SELECT 1 FROM User WHERE Email = 1@1.com"
    print(cursor.execute(sql))
    cursor.close()
    return True