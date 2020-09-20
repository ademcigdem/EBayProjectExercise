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

    private static Logger logger = Logger.getLogger(String.valueOf(Hooks.class));
    @Before
    public void setup(Scenario scenario){
        System.out.println(scenario.getName());
        Driver.get().manage().window().maximize();
        logger.info("Before Setup Started");
  //      Driver.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown(Scenario scenario){
        if (scenario.isFailed()){
            final byte[] screenshot = ((TakesScreenshot)Driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png","screenshot");
        }
        logger.info("After TearDown");
        Driver.closeDriver();
    }


}
