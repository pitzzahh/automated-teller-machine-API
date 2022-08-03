package io.github.pitzzahh.entity;

/**
 * record used to generate message.
 * @param request the loan request.
 * @param message the message
 */
public record Message(Loan request, String message) { }
