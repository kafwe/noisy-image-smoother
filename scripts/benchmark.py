import os

window_widths = [3, 5, 11, 15]
filters = ["MedianFilterSerial", "MeanFilterParallel", "MedianFilterParallel", 
"MeanFilterSerial", ]

for x in range(1, 5):
    for filter in filters:
        file_name = f"galactic{x}"
        input_image = f"images/input/{file_name}.jpg"
        output_image = f"images/{filter}/{file_name}-{filter}.jpg"
        result_file = f"results/{filter}/{file_name}.txt"

        for window_width in window_widths:
            print(f"Testing {file_name}.jpg with window width {window_width}")

            with open(result_file, "a") as file:
                file.write(f"{file_name}, window width={window_width}\n")

            for i in range(5):
                os.system(f"java -cp bin {filter} {input_image} {output_image} \
                {window_width} >> {result_file}")
        
        print("-"*50)

print("DONE")