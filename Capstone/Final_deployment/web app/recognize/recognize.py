#!/usr/bin/python
import cv2
import numpy as np

# color perematers
red_min = np.array([0, 128, 10])
red_max = np.array([30, 255, 255])
blue_min = np.array([60, 128, 46])
blue_max = np.array([124, 255, 255])


# start function
def image_recoginition(image_path):
    return choose_image(image_path)


# choose image
def choose_image(image_path):
    input_image, contours = image_read(cv2.imread(image_path))
    input_image = cv2.resize(input_image, (512, 512), interpolation=cv2.INTER_CUBIC)
    volume, medicine = whole_syringe(input_image, contours)
    return volume, medicine


def exit_program():
    exit()


# read and pre-process image
def image_read(image):
    # Canny method
    img_reading = image.copy()
    img_reading = cv2.cvtColor(img_reading, cv2.COLOR_BGR2GRAY)
    img_reading = cv2.resize(img_reading, (512, 512), interpolation=cv2.INTER_CUBIC)

    img_reading = cv2.GaussianBlur(img_reading, (3, 3), 0)
    img_canny = cv2.Canny(img_reading, 40, 120)
    contours, hisrarchy = cv2.findContours(img_canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

    return image, contours


# %%

# color recongnition return medicine type
def color_recognition(image):
    medicine = 'Advil'
    image_reading = image.copy()
    hsv = cv2.cvtColor(image_reading, cv2.COLOR_BGR2HSV)

    # red
    mask_red = cv2.inRange(hsv, red_min, red_max)
    res_red = cv2.bitwise_and(hsv, hsv, mask=mask_red)
    res_red = cv2.cvtColor(res_red, cv2.COLOR_HSV2BGR)
    res_red = cv2.cvtColor(res_red, cv2.COLOR_BGR2GRAY)
    contours_red, hisrarchy = cv2.findContours(res_red, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    count2 = 0
    for contours in contours_red:
        x, y, w, h = cv2.boundingRect(contours)
        if w > 1 and h > 1:
            count2 = count2 + 1
    if count2 > 1:
        medicine = 'Panadol'

    # blue
    mask_blue = cv2.inRange(hsv, blue_min, blue_max)
    res_blue = cv2.bitwise_and(hsv, hsv, mask=mask_blue)
    res_blue = cv2.cvtColor(res_blue, cv2.COLOR_HSV2BGR)
    res_blue = cv2.cvtColor(res_blue, cv2.COLOR_BGR2GRAY)
    contours_blue, hisrarchy = cv2.findContours(res_blue, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
    count = 0
    for contours in contours_blue:
        x, y, w, h = cv2.boundingRect(contours)
        if w > 1 and h > 1:
            count = count + 1
    if count > 1:
        medicine = 'Demazin'

    return medicine


# %%

def volume_by_edge(height, width):
    whole_syringe_size_mm = 35
    mm_per_ml = 2.5
    tip_mm = 7
    plug_mm = 85
    mm_per_pixel = whole_syringe_size_mm / width
    whole_length = height * mm_per_pixel
    medicine_length = whole_length - plug_mm - tip_mm
    medicine_volume = medicine_length * (1 / mm_per_ml)
    return medicine_volume


# %%

# determine volume by boxing the whole syringe
def whole_syringe(input_image, contours):
    x_start, y_start, w_s, h_s = cv2.boundingRect(contours[0])
    x_end = x_start + w_s
    y_end = y_start + h_s
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        if y < y_start:
            y_start = y
        if x < x_start:
            x_start = x
        if x + w > x_end:
            x_end = x + w
        if y + h > y_end:
            y_end = y + h
    whole_height = abs(y_end - y_start)
    whole_wide = abs(x_end - x_start)
    volume = volume_by_edge(whole_height, whole_wide)
    medicine = color_recognition(input_image)

    return volume, medicine

