module com.whitelaw.quizinator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires org.apache.commons.lang3;
    requires commons.text;

    opens com.whitelaw.quizinator to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.whitelaw.quizinator;
}