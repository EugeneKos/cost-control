package org.eugene.cost.service.impl;

import org.eugene.cost.config.SpringConfiguration;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.ISessionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class SessionServiceImplTest {
    @Autowired
    private ISessionService sessionService;

    @Test
    public void createTest(){
        LocalDate beginDate = LocalDate.now();
        LocalDate finalDate = beginDate.plusDays(25);

        Session session = sessionService.create("20000", beginDate, finalDate);

        List<Day> days = session.getDays();
        Assert.assertNotNull(days);

        Assert.assertEquals("compare num days", 26, days.size());

        sessionService.calculateMediumLimit(session);
        System.out.println(session);
    }
}
