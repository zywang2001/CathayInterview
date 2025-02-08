package com.example.demo;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/insert")
    public CoinType insert(@RequestBody CoinType coin){
        return coinService.insert(coin);
    }

    @PostMapping("/insert_multi")
    public List<CoinType> insert_multi(@RequestBody List<CoinType> coins){
        return coinService.insert_multi(coins);
    }

    @GetMapping("/selectAll")
    public List<CoinType> selectAll() {
        return coinService.selectAll();
    }

    @GetMapping("/selectById/{id}")
    public Optional<CoinType> selectById(@PathVariable int id){
        return coinService.selectById(id);
    }

    @PutMapping("/update/{id}")
    public CoinType update(@PathVariable int id,
                        @RequestBody CoinType coin) {
        coin.setId(id);
        return coinService.update(coin);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id){
        coinService.delete(id);
    }
    
    @GetMapping("/getCoindeskData")
    public String getCoindeskData() throws IOException {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @GetMapping("/convertCoindesk")
    public  Map<String, Object> convertCoindesk() throws IOException {       
        String resData = getCoindeskData();
        JSONObject jsonObject = new JSONObject(resData); 

        String responds_time = jsonObject.getJSONObject("time").getString("updatedISO");
        String formattedRespondsTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"))
                .format(Instant.parse(responds_time));

        Instant now = Instant.now();
        String formattedNowTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"))
                .format(now);

        // -        
        JSONArray resultArr = new JSONArray();
        JSONObject bpiObject = jsonObject.getJSONObject("bpi");
        for (String coinCode : bpiObject.keySet()) {
            JSONObject coin = bpiObject.getJSONObject(coinCode);
            String rate = coin.getString("rate");
            String desc = coin.getString("description");

            String bpiInfo = String.format("coinCode:%s, rate:%s, description:%s",coinCode, rate, desc);
            resultArr.put(bpiInfo);
        }

        JSONObject res = new JSONObject();
        res.put("respondsTime", formattedRespondsTime);
        res.put("NowTime", formattedNowTime);
        res.put("resultOfBpiInfo", resultArr);
        return res.toMap();
    }

}
