package com.example.parkscout.Model

public class ChatMessage {

    // Data Members
    public var sender: String
    public var receiver: String
    public var message: String


    // Constructors
    constructor(sender: String, receiver: String, message: String) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
    }
}