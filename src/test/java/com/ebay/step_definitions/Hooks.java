package com.ebay.step_definitions;
import com.ebay.utils.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


@Slf4j
public class Hooks {

    private static final Logger LOGGER = Logger.getLogger(String.valueOf(Hooks.class));
    @Before
    public void setup(Scenario scenario){
        System.out.println(scenario.getName());
        Driver.get().manage().window().maximize();
        LOGGER.info("Before Setup Started");
     //   Driver.get().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @After
    public void tearDown(Scenario scenario){
        if (scenario.isFailed()){
            final byte[] screenshot = ((TakesScreenshot)Driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png","screenshot");
        }
        LOGGER.info("After TearDown");
        Driver.closeDriver();
    }


}
