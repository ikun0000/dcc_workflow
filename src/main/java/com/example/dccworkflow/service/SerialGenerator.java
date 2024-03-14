package com.example.dccworkflow.service;

import com.example.dccworkflow.entity.SerialNumber;
import com.example.dccworkflow.repository.SerialNumberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SerialGenerator {
    private SerialNumberRepository serialNumberRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    private static final Lock lock = new ReentrantLock();

    public SerialGenerator(SerialNumberRepository serialNumberRepository) {
        this.serialNumberRepository = serialNumberRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetSerial() {
        lock.lock();
        try {
            for (SerialNumber serialNumber : serialNumberRepository.findAll()) {
                serialNumber.setNumber(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public String getSeralNum(String prefix) {
        lock.lock();
        try {
            LocalDate today = LocalDate.now();

            // 项目运行前应当手动初始化，理论上绝不可能为空，如果为空一定是人的问题
            SerialNumber serialNumber = serialNumberRepository.findById(1L).get();
            serialNumber.setNumber(serialNumber.getNumber() + 1);

            return String.format("%s%s%04d",
                    "PJ." + prefix + ".",
                    DATE_FORMAT.format(today),
                    serialNumber.getNumber() - 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
