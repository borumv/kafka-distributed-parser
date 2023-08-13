# Kafka distributed csv-parser

## 🔎 Overview 🔎
This project demonstrates a distributed data processing solution using Apache Kafka and its Kafka Streams API. The primary goal is to perform a GroupBy operation on data from a CSV file with two columns.

## 🗂️ Table of Contents 🗂️
- [Project Overview](#Overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Roadmap and Issues](#Project-Roadmap-and-Issues)
    - [Current Issues](#Current-Issues)
- [Getting Started](#getting-started)
    - [Installation](#installation)
    - [Running the Application](#Running-the-Application)
  

## Project Modules

The program is divided into the following modules:

1. **kafka-distributer**: This module serves as the entry point for our project. Through the command line, we can specify the file for which we want to perform a GroupBy operation. For splitting, we utilize a separate splitter, which can be found in another repository at [link to splitter repository](link-to-splitter-repository).

2. **kafka-distributor-split-to-stream**: This module is responsible for the distributed reading of a file. It receives messages in the format `{leftIndex:number_left_index, rightIndex:numberIndex, path:pathToFile}` as input and generates messages for the `stream-input` topic.

3. **kafka-distributor-stream-to-group-by**: This module generates the resulting GroupBy using Kafka Streams. It retrieves data from the `stream-input` stream and aggregates the result into the `stream-output` topic. The `kafka-distributer` client module receives the final result from the `stream-output` topic.

The `kafka-distributor-split-to-stream` and `kafka-distributor-stream-to-group-by` modules are designed for extensibility to provide a distributed processing system. This program serves as an example solution to demonstrate distributed processing capabilities.

## Features

- Distributed data processing
- Utilization of Kafka Streams for data aggregation
- Modular architecture for enhanced scalability

## Technologies Used

- Spring boot
- Apache Kafka
- Kafka Streams API
- Docker (for Kafka server)


# Project Roadmap and Issues

## Current Issues

- Writing tests for modules


# Getting Started

## Installation

1. Clone this repository.
2. Set up Docker to run the Kafka server using the provided `docker-compose.yml` file.


## Running the Application

1. Start the Kafka server using Docker.
2. Run the `kafka-distributor-split-to-stream` module.
3. Execute the `kafka-distributor-stream-to-group-by` module.
4. Launch the `kafka-distributer` module, specifying the path to the CSV file or using the provided example in the `resources` folder.


# User Interface Overview
