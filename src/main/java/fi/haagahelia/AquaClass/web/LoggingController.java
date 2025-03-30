package fi.haagahelia.AquaClass.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @GetMapping("/log")
    public String logExample() {
        logger.trace("This is a TRACE log");
        logger.debug("This is a DEBUG log");
        logger.info("This is an INFO log");
        logger.warn("This is a WARN log");
        logger.error("This is an ERROR log");

        return "Logs have been written. Check your console or file!";
    }
}

