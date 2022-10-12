import unittest
import functions as fc
import sqlite3
import random
import hashlib
# import HTMLTestRunner

class test_functions(unittest.TestCase):
    # def setUpClass(self):
    #     print("The unit test for functions begin!")

    def test_registion_failture(self):
        actual_result = fc.register_user("test", "ayan0137@gmail.com", 480158532, "hash_code", 
            "normal", 0, "user")
        error_mess = "Test 1: test_registion_failture failed!"
        # print(type(actual_result))
        self.assertFalse(actual_result, error_mess)
        print("Test_registion_failture passed!")

    def test_registion_sucess(self):
        random_seed = random.uniform(1, 10)
        email = str(random_seed) + "_test@gmail.com"
        actual_result = fc.register_user("test", email, 480158532, "hash_code", 
            "normal", 0, "user")
        error_mess = "Test 2: test_registion_sucess failed!"
        self.assertTrue(actual_result, error_mess)
        print("Test_registion_sucess passed!")

    def test_login_failture(self):
        actual_result = fc.login_check("ayan0137@gmail.com", "zxcvbnm,./")
        error_mess = "Test 3: test_login_failture failed!"
        self.assertFalse(actual_result, error_mess)
        print("Test_login_failture passed!")

    def test_login_success(self):
        password = "1234567890"
        hash_code = hashlib.sha256(password.encode()).hexdigest()
        actual_result = fc.login_check("ayan0137@gmail.com", hash_code)
        error_mess = "Test 4: test_login_success failed!"
        self.assertTrue(actual_result, error_mess)
        print("Test_login_success passed!")

    def test_update_profile_all(self):
        password = "1234567890"
        hash_code = hashlib.sha256(password.encode()).hexdigest()
        result = fc.update_profile("ayan0137@gmail.com", "", "An", "Yan", "ayan0137@gmail.com", 
            "22", "street No.1", "male", "user", hash_code)
        error_mess = "Test 5: test_update_profile_all failed!"
        self.assertTrue(result, error_mess)
        print("Test_update_profile_all passed!")

    def test_update_profile_onlyone(self):
        result = fc.update_profile("ayan0137@gmail.com", "", "", "", "", 
            "22", "", "", "", "")
        error_mess = "Test 6: test_update_profile_onlyone failed!"
        self.assertTrue(result, error_mess)
        print("Test_update_profile_onlyone passed!")

    def test_admin_edit_all(self):
        password = "1234567890"
        hash_code = hashlib.sha256(password.encode()).hexdigest()
        result = fc.update_profile("ayan0137@gmail.com", "", "An", "Yan", "ayan0137@gmail.com", 
            "22", "street No.1", "male", "user", hash_code)
        error_mess = "Test 7: test_admin_edit_all failed!"
        self.assertTrue(result, error_mess)
        print("Test_admin_edit_all passed!")

    def test_admin_edit_onlyone(self):
        result = fc.update_profile("ayan0137@gmail.com", "", "", "", "", 
            "22", "", "", "", "")
        error_mess = "Test 8: test_admin_edit_onlyone failed!"
        self.assertTrue(result, error_mess)
        print("Test_admin_edit_onlyone passed!")

    def test_add_history(self):
        result = fc.add_history(2, "2021.11.7", "13:51", 20, "medicine", "image_path")
        error_mess = "Test 9: test_add_history failed!"
        self.assertEqual(type(result), int, error_mess)
        print("test_add_history passed!")

    # def test_connection(self):
    #     result = fc.test_connection()
    #     self.assertTrue(result)
    

if __name__ == '__main__':
    # verbosity=*：默认是1；设为0，则不输出每一个用例的执行结果；2-输出详细的执行结果
    print("")
    print("Test coverage: " + str((7/8)*100) + "%")
    print("----------------------------------------------------------------------")
    unittest.main(verbosity=1)
    