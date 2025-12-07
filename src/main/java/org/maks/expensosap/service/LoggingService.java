package org.maks.expensosap.service;

import org.maks.expensosap.model.LogEntry;
import org.maks.expensosap.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggingService {

    private final LogEntryRepository logRepo;

    public void log(String event, Long userId, Object meta) {
        LogEntry entry = LogEntry.builder()
                .event(event)
                .userId(userId)
                .meta(meta != null ? meta.toString() : null)
                .build();

        logRepo.save(entry);
    }
}

