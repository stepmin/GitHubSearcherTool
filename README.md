# Simple GitHub Searcher

## Overview
The **GitHub Repository Searcher** is an Android application developed with Jetpack Compose, GraphQL, and Room for local storage. This app allows users to search for GitHub repositories, star/unstar them. It is built with Kotlin Multiplatform capabilities, allowing the potential to extend it to iOS.

## Features

### 1. GitHub Repository Search
- Users can search for repositories using the GitHub REST API.
- The search results are displayed with pagination to efficiently manage large sets of repositories.

### 2. Star/Unstar Repositories
- Users can star or unstar repositories directly from the app.
- These actions are performed using the GitHub GraphQL API.

### 3. Local Storage
- Data is synchronized between local and remote sources to ensure consistency.

### 4. Authentication
- Authentication is handled using a GitHub personal access token (PAT) that lasts for 30 days.

### 5. Kotlin Multiplatform (Optional)
- The core logic can be shared between Android and iOS platforms.

## Technologies Used
- **Kotlin Multiplatform**: Core logic for API calls and data management.
- **Jetpack Compose**: UI framework for building the Android interface.
- **GraphQL**: Used for starring/un-starring repositories.
- **REST API**: Used for searching GitHub repositories.
- **Kotlin Coroutines & Flow**: For handling asynchronous operations and real-time data updates.
- **Room Database**: For local data storage and caching.
- **Git**: Version control for maintaining the project.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/stepmin/GitHubSearcherTool.git
