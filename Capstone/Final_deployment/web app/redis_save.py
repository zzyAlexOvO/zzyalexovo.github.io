import redis
import base64


def __connection(ip, port):
    return redis.Redis(host=ip, port=port, decode_responses=True)


def return_img_stream(img_local_path):
    """
    工具函数:
    获取本地图片流
    :param img_local_path:文件单张图片的本地绝对路径
    :return: 图片流
    """
    import base64
    img_stream = ''
    with open(img_local_path, 'r') as img_f:
        img_stream = img_f.read()
        img_stream = base64.b64encode(img_stream)
    return img_stream


def store_image(user_id, image_id, extend, image, ip, port):
    # base64_data = __encode_image(image)
    r = __connection(ip, port)
    r.set(str(user_id) + str(image_id) + str(extend), image)
    r.close()


def get_image(user_id, image_id, extend, ip, port):
    r = __connection(ip, port)
    image_base64 = r.get(user_id + image_id + extend)
    r.close()
    return image_base64


def test_set():
    r = __connection("172.31.2.4", "6379")
    r.set('name', 'lansgg')
    r.get('name')
    print(r.exists('name'))
    r.delete('name')
    r.dbsize()


def test_store_images():
    store_image(0, 0, "test", open("static/images/tmp.jpg"), "1782.31.2.4", 6379)


def test_get_images():
    store_image(0, 0, "test","1782.31.2.4", 6379)
