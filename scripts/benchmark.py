"""Script to automate the benchmarking of both the sequential and 
parallel filters"""
import os

window_widths = [3, 5, 11, 15]
filters = ["MedianFilterSerial", "MeanFilterParallel", "MedianFilterParallel", 
"MeanFilterSerial"]

for x in range(1, 6):
    for filter in filters:
        file_name = f"galactic{x}"
        input_image = f"images/input/{file_name}.jpg"
        output_image = f"images/{filter}/{file_name}-{filter}.jpg"
        result_file = f"results/{filter}/{file_name}.txt"

        for window_width in window_widths:
            print(f"Benchmarking {filter} with window width {window_width} on {file_name}.jpg ")

            with open(result_file, "a") as file:
                file.write(f"{file_name}, window width={window_width}\n")

            for i in range(5):
                os.system(f"java -cp bin {filter} {input_image} {output_image} \
                {window_width} >> {result_file}")

    print("{:-^70}".format(str(20*x) + "% done"))

print("DONE")