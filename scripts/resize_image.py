"""Resizes a very large image to make images of varied sizes for use in the benchmarking"""
from PIL import Image
from sys import argv

file_name = argv[1]

with Image.open(f"images/input/{file_name}.jpg") as image:
    width = image.size[0]
    height = image.size[1]

    for i in range(4, 0, -1):
        width = width // 2
        height = height // 2
        resized_image = image.resize((width, height))
        resized_image.save(f"images/input/{file_name + str(i)}.jpg")