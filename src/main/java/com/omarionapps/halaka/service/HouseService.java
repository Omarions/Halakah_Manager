package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.House;
import com.omarionapps.halaka.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

	public Optional<House> findById(Integer id) {
		return houseRepository.findById(id);
    }

    public Map<String, Integer> getHousingOccupyingMap(){
        Iterable<House>      list = this.findAllByOrderById();
        Map<String, Integer> map  = new HashMap<>();

        list.forEach((house) -> {
            if(house != null)
                map.put(house.getName(), house.getStudents().size());
        });
	    //System.out.println(map);
        return map;
    }

    public House findByName(String name) {
        return houseRepository.findHouseByName(name);
    }

	public List<House> findAllByOrderById() {
        return houseRepository.findAllByOrderById();
    }

    public Map<String, Integer> getHousingCapacityMap(){
        Map<String, Integer> map  = new HashMap<>();
        Iterable<House>      list = this.findAllByOrderById();
        list.forEach((house) -> {
            map.put(house.getName(), house.getCapacity());
        });
	    //System.out.println(map);
        return map;
    }


    public int getTotalOccupied(){
        int             count    = 0;
        Iterable<House> list     = this.findAllByOrderById();
        Iterator<House> iterator = list.iterator();
        while(iterator.hasNext()){
            count += iterator.next().getStudents().size();
        }
	    //System.out.println("Total Busy Setas: " + count);
        return count;
    }

    public int getTotalFree(){
        int             count    = 0;
        Iterable<House> list     = this.findAllByOrderById();
        Iterator<House> iterator = list.iterator();
        while(iterator.hasNext()){
            House house = iterator.next();
            count += (house.getCapacity() - house.getStudents().size());
        }
	    //System.out.println("Total Free Seats: " + count);
        return count;
    }

    public int getTotalCapacity(){
        int             count    = 0;
        Iterable<House> list     = this.findAllByOrderById();
        Iterator<House> iterator = list.iterator();
        while(iterator.hasNext()){
            count += iterator.next().getCapacity();
        }
        return count;
    }

}
