# Parallel Word Search with Synchronization

This Java application performs a parallelized word search across an input text file. It divides the text into multiple sections and uses multiple threads to search for specific words (provided in a second input file). The results, including the word, its line number, and the thread that processed it, are logged into an output file. 

## Key Features:
- **Parallel Processing**: Utilizes `ExecutorService` to manage multiple threads, each processing a different section of the input text.
- **Concurrency Control**: Provides two methods of thread synchronization:
  - `ReentrantLock` (mutex)
  - `Semaphore`
- **Custom Word Search**: Searches for words provided in a separate input file.
- **File Handling**: Records the results with timestamp information into an output file.
  
## Files:
- **Main.java**: Manages the execution, file reading, and sectioning of the input.
- **Mutex.java**: A thread task using a `ReentrantLock` for thread synchronization.
- **ProcessWithSemaphore.java**: A thread task using a `Semaphore` for synchronization.
- **Processing.java**: A simplified version of the process without advanced synchronization.
- **WordWithLine.java**: Stores word-line pairs for processing.

## Performance:
- The program efficiently handles concurrent processing and synchronization, but the file-writing mechanism could be optimized further to avoid overwriting the output file multiple times.

## Usage:
1. Place your input text file (`input.txt`) and word list (`inputWord.txt`) in the root directory.
2. Compile and run `Main.java`.
3. Check `output.txt` for the results.

## Requirements:
- Java 8 or higher
- No external dependencies

