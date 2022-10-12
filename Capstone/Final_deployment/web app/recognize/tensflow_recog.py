#!/usr/bin/python
import cv2
import tensorflow as tf

CATES = ["Advil", "Demazin", "Panadol"]


def prepare(filepath):
    IMG_SIZE = 400  # 50 in txt-based
    img_array = cv2.imread(filepath)
    new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))
    return new_array.reshape(-1, IMG_SIZE, IMG_SIZE, 3)


def recognize(file_path):
    model = tf.keras.models.load_model("robCNN3.model")

    prediction = model.predict([prepare(file_path)])
    # print(prediction)  # will be a list in a list.
    return CATES[int(prediction[0][0])]
