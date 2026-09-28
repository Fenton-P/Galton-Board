# Galton Board - A Monte Carlo Simulation with AWS Lambda
This project simulates a Galton Board using the Monte Carlo simulation technique. The algorithm starts by simulating a ball, it then calculates the probability the ball will go left or right and updates its position depending on a random value given those probabilities. It repeats this until it 'falls' into a 'bin' (iterates a set number of times). Then increments the corresponding value in the bin it falls into. The bins are displayed using a bar chart with 0 being the first bin and 49 being the last.  

## To run this program, follow these steps:
1. Download at least JRE 21 or above ([here](https://www.oracle.com/java/technologies/downloads/#java21) is a link to a JDK download for java 21)
2. Download the project as an executable jar (under the builds folder of the project)
3. Run the project and enjoy!

## How to use this program
First understand what a Galton board is:  

<img width="250" height="312" alt="galton_board" src="https://github.com/user-attachments/assets/41c83df8-b914-4979-b188-09bc5db1eb52" />  

The image above shows what a Galton board looks like, essentially a board where you drop a ball down and it has a 50/50 chance of bouncing either left or right. When you drop enough of the balls down, a bell curve (normal distribution) will appear on the board. This is shown with the line and bar charts in the image.  

Now this simulation requires a few inputs when you first open the program:
1. **The Batch Size** - this will specify how many 'balls' the program will drop per batch
2. **The Bin Count** - this will specify the size of the simulation: more bins equals to a larger Galton board
3. **The Bias Jump** - this is an augmentation to a traditional Galton board. When the ball switches direction (or just starts), the ball's likelihood of continuing in the new direction gets set to the base probability plus Bias Jump
4. **The Bias Adjustment** - this will specify how much the bias changes with each round based on the ball's direction. If the bias adjustment value is positive, and the ball is traveling to the right, then the ball will decrease its odds of traveling right
5. **The General Drift** - I mentioned the 'base probability' before, essentially the General Drift will alter the traditional probability (50/50 odds) in such a manner that the ball will have a 60/40 percent chance of traveling to the right if the General Drift value is 0.1
6. **The Bias Clamp** - this determines the minimum and maximum values of bias, where clamp = .5 will limit bias to -.5 to .5 and cover completely biased one way to the other. However, playing with Bias Clamp above .5 or 1 creates interesting 'duopolies' or mounds
7. **The Graph Start** - choose the bin to start rendering from (usually 0 unless you want to render a buuuunch of bins because the graph will not display if you make it too small)
8. **The Graph End** - same deal as the Graph Start but at the end
9. **The Batch Count** - this mainly applies for the AWS mode (explained below) but this will run Batch Count number of batches or Batch Count times Batch Size number of balls
10. **The Mode** - either CPU or AWS. CPU will execute on the CPU. AWS will use Amazon Web Services to run a Lambda function that will simulate Batch Size number of balls through a request to AWS Lambda. There will be Batch Count number of requests made concurrently such that they are parallel and being computed at the same time
11. **The CPU Delay** - will add a pause between patches on the CPU to show what is happening and add a little animation-ish

After providing all of this information, simply click update and watch the simulation do its thing.  

## Examples
Provided below are some sample images of what the program looks like when executing:  

<img height="300" alt="image" src="https://github.com/user-attachments/assets/efc84215-7b6a-4a91-aa01-3a9a498b7c6e" />    

<img height="300" alt="image" src="https://github.com/user-attachments/assets/a061dd4f-5a45-426e-a4c2-26cbe9ac7bad" />
