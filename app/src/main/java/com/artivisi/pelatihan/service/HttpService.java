package com.artivisi.pelatihan.service;

import com.artivisi.pelatihan.domain.Peserta;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by opaw on 8/26/17.
 */

public class HttpService {

    private static final String HOST = "http://192.168.1.50:8080";
    private static final String BASE_URL = HOST + "/api/";
    private RestTemplate restTemplate = new RestTemplate();

    public HttpService(){
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public List<Peserta> getAllPeserta(){
        String url = BASE_URL + "peserta";
        ResponseEntity<Peserta[]> resp = restTemplate.exchange(url, HttpMethod.GET, null, Peserta[].class);
        List<Peserta> pesertaList = Arrays.asList(resp.getBody());
        return pesertaList;
    }

    public Void deletePeserta(String id){
        String url = BASE_URL + "peserta?id="+id;
        ResponseEntity<Void> resp = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        return resp.getBody();
    }

    public Void savePeserta(Peserta p){
        String url = BASE_URL + "peserta";
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Peserta> httpEntity = new HttpEntity<>(p, httpHeaders);
        ResponseEntity<Void> resp = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
        return resp.getBody();
    }
}
