import os


WINDOW_WIDTH = 15
FILTER = "MedianFilterParallel"
RESULTS_DIR = f"results/sequential_cutoff/{FILTER}"

for x in range(1, 5):
    file_name = f"galactic{x}"
    input_image = f"images/input/{file_name}.jpg"
    output_image = f"images/sequential_cutoff/{file_name}-{FILTER}.jpg"

    for i in range(1, 11):
        sequential_cutoff = 2 ** i
        print(f"Testing {file_name}.jpg with cutoff {sequential_cutoff}")

        with open(f"{RESULTS_DIR}/{file_name}.txt", "a") as file:
            file.write(f"{file_name}, cutoff={sequential_cutoff}\n")

        for j in range(5):
            os.system(f"java -cp bin {FILTER} {input_image} {output_image} \
             {WINDOW_WIDTH} {sequential_cutoff} >> {RESULTS_DIR}/{file_name}.txt")
    
    print("-"*50)
