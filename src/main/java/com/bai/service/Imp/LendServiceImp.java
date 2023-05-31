package com.bai.service.Imp;

import com.bai.dao.LendMapper;
import com.bai.pojo.Lend;
import com.bai.service.LendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LendServiceImp implements LendService {

    @Autowired
    private LendMapper lendMapper;

    @Override
    public int addLend(Lend lend, String bookId) {
        lendMapper.decBookNum(Long.parseLong(bookId));
        return lendMapper.addLend(lend);
    }

    @Override
    public List<Lend> queryMyLend(long readerId) {
        return lendMapper.queryMyLend(readerId);
    }

    @Override
    public int backBook(long bookId) {
        lendMapper.incBookNum(bookId);
        return lendMapper.backBook(bookId);
    }
}
