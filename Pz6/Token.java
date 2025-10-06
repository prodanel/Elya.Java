package Pz6;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

    class Token {
        public enum Type {
            NUMBER, VARIABLE, OPERATOR, FUNCTION, LEFT_PAREN, RIGHT_PAREN
        }

        private Type type;
        private String value;
        private int precedence;

        public Token(Type type, String value) {
            this.type = type;
            this.value = value;
            switch (value) {
                case "+": case "-": this.precedence = 1; break;
                case "*": case "/": this.precedence = 2; break;
                case "^": this.precedence = 3; break;
                case "unary-": this.precedence = 4; break;
                default: this.precedence = 0;
            }
        }

        public Token(Type type, String value, int precedence) {
            this.type = type;
            this.value = value;
            this.precedence = precedence;
        }

        public Type getType() { return type; }
        public String getValue() { return value; }
        public int getPrecedence() { return precedence; }

        @Override
        public String toString() {
            return value;
        }
    }

