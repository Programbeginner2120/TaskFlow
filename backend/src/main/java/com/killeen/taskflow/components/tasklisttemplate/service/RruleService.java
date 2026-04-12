package com.killeen.taskflow.components.tasklisttemplate.service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.killeen.taskflow.components.tasklisttemplate.exception.InvalidRruleException;

import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.model.Recur;

@Service
@RequiredArgsConstructor
public class RruleService {

    private final Environment env;

    public OffsetDateTime computeNextGenerate(String rrule, String timezone) {
        try {
            Recur<ZonedDateTime> recur = new Recur<>(rrule);
            ZonedDateTime now  = ZonedDateTime.now(ZoneId.of(timezone));
            ZonedDateTime next = recur.getNextDate(now, now);
            if (next == null) {
                throw new InvalidRruleException(
                        env.getProperty("rrule.no.future.occurrences") + ": " + rrule);
            }
            return next.toOffsetDateTime();
        } catch (InvalidRruleException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidRruleException(
                    env.getProperty("rrule.invalid") + ": " + rrule);
        }
    }
}
