Telegram Bot with Spring Boot:


This project is a Telegram Bot implemented using Java and Spring Boot. It provides various features like handling text messages, sending media files, creating polls, and integrating interactive buttons, making it an extensible and robust bot framework for diverse use cases.

Test instructions:

/poll <question> <option1,option2,...>

example:

/poll Invest in Bitcoin or Etherium? Bitcoin,Etherium,None


Features:

Interactive Polls: Allows creating polls with inline buttons for user responses.

Inline Keyboards: Adds buttons for enhanced user interaction.

Scalable Design: Built using the MVC architecture, making it extensible for additional features like meeting scheduling, reminders, and more.


Technology Stack

Java 17 (Compatible with Corretto 17)

Spring Boot (For building REST APIs and business logic)

Telegram Bot API (For communication with Telegram servers)

Maven (Dependency and build management)


Project Structure

controller - Manages requests and integrates with services to handle bot interactions.

service - Contains business logic for handling features like polls and media messages.

model - Includes data models for the bot, such as Poll.

bot - Implements the Telegram Bot using TelegramLongPollingBot.


