package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    public List<CoinType> selectAll() {
        return coinRepository.findAll();
    }

    public Optional<CoinType> selectById(int id) {
        return coinRepository.findById(id);
    }

    public CoinType insert(CoinType c) {
        return coinRepository.save(c);
    }

    public List<CoinType> insert_multi(List<CoinType> coin_list) {
        return coinRepository.saveAll(coin_list);
    }

    public CoinType update(CoinType coin){
        return coinRepository.save(coin);
    }

    public void delete(int id){
        coinRepository.deleteById(id);
    }

}
