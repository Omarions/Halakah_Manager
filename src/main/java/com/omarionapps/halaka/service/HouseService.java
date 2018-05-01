package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.House;
import com.omarionapps.halaka.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Omar on 15-Apr-17.
 */
@Service
public class HouseService {
    private HouseRepository houseRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository){
        this.houseRepository = houseRepository;
    }

	public House findById(Integer id) {
        return houseRepository.findOne(id);
    }

    public Iterable<House> findAllOrderById(){
        return houseRepository.findAll();
    }

	public House findByName(String name) {
		return houseRepository.findHouseByName(name);
	}
    public Map<String, Integer> getHousingOccupyingMap(){
        Iterable<House> list = this.findAllOrderById();
        Map<String, Integer> map = new HashMap<>();

        list.forEach((house) -> {
            if(house != null)
                map.put(house.getName(), house.getStudents().size());
        });
        System.out.println(map);
        return map;
    }

    public Map<String, Integer> getHousingCapacityMap(){
        Map<String, Integer> map = new HashMap<>();
        Iterable<House> list = this.findAllOrderById();
        list.forEach((house) -> {
            map.put(house.getName(), house.getCapacity());
        });
        System.out.println(map);
        return map;
    }


    public int getTotalOccupied(){
        int count = 0;
        Iterable<House> list = this.findAllOrderById();
        Iterator<House> iterator = list.iterator();
        while(iterator.hasNext()){
            count += iterator.next().getStudents().size();
        }
        System.out.println("Total Busy Setas: " + count);
        return count;
    }

    public int getTotalFree(){
        int count = 0;
        Iterable<House> list = this.findAllOrderById();
        Iterator<House> iterator = list.iterator();
        while(iterator.hasNext()){
            House house = iterator.next();
            count += (house.getCapacity() - house.getStudents().size());
        }
        System.out.println("Total Free Seats: " + count);
        return count;
    }

    public int getTotalCapacity(){
        int count = 0;
        Iterable<House> list = this.findAllOrderById();
        Iterator<House> iterator = list.iterator();
        while(iterator.hasNext()){
            count += iterator.next().getCapacity();
        }
        return count;
    }

}
