# noisy-image-smoother

Implemented serial and parallel 2D filters for smoothing RGB colour images. Benchmarked the parallel
programs to the serial ones to determine under which conditions parallelization is worth the extra effort
involved.

An example image filtered by the program:

![filter](https://user-images.githubusercontent.com/75791207/205197734-89c12ba6-2af9-41cf-8c6f-e41e8c70cc20.png)

## Method
- Wrote two serial programs to apply a mean and median filters to a specified
image.
- Wrote divide-and-conquer parallel programs using the Java Fork/Join
framework in order to speed up the median and the mean filter process. It was a requirement to have **4 seperate programs** with no dependencies between each other. 
- Benchmarked the serial and parallel programs experimentally by timing the execution on
two machines with different image sizes and different filter sizes (e.g 3x3,5x5,11x11,15x15), generating
speedup graphs that show when the best parallel speedup is achieved.
- Wrote Python scripts to automate resizing the images and also to conduct the benchmarking with the different filter window sizes. The scripts also saved the results. 
- Wrote a short report including the graphs with an explanation the findings. 

## Folder Structure

- `src`: the folder to maintain sources
- `scripts`: the folder containing all the Python scripts
- `results`: the folder containing all the results from the benchmarking
- `tests`: the folder containing the tests that prove the correctness of the parallel programs
