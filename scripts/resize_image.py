#!/usr/bin/python3
"""Resizes a big image 3 times to make images for use in the benchmarking"""
from PIL import Image
from sys import argv

file_name = argv[0]

with Image.open(f"images/input/{file_name}.jpg") as image:
    width = image.size[0]
    height = image.size[1]

    for i in range(4, 1, -1):
        resized_image = image.resize(round(width/i), round(height/i))
        resized_image.save(f"images/input/{file_name + str(i)}.jpg")