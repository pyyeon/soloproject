package com.springboot.coffee.service;

import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.repository.CoffeeRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoffeeService {

    CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    public Coffee createCoffee(Coffee coffee) {
        String coffeeCode = coffee.getCoffeeCode().toUpperCase();
        verifyExistCoffee(coffeeCode);
        coffee.setCoffeeCode(coffeeCode); // 대문자로 들어가야함
        return coffeeRepository.save(coffee);
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public Coffee updateCoffee(Coffee coffee) {
        Coffee findCoffee = findVerifiedCoffee(coffee.getCoffeeId());

        Optional.ofNullable(coffee.getKorName())
                .ifPresent(korName -> findCoffee.setKorName(korName));

        Optional.ofNullable(coffee.getEngName())
                .ifPresent(engName -> findCoffee.setEngName(engName));

        Optional.ofNullable(coffee.getPrice())
                .ifPresent(price -> findCoffee.setPrice(price));

        Optional.ofNullable(coffee.getCoffeeStatus())
                .ifPresent(coffeeStatus -> findCoffee.setCoffeeStatus(Coffee.CoffeeStatus.COFFEE_FOR_SALE));
        return coffeeRepository.save(coffee);
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public Coffee findCoffee(long coffeeId) {
        return findVerifiedCoffeeByQuery(coffeeId);
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    // 주문에 해당하는 커피 정보 조회
    public List<Coffee> findOrderedCoffees(Order order) {
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        List<OrderCoffee> orderCoffees = order.getOrderCoffees();
        List<Coffee> coffees = orderCoffees.stream()
                .map(orderCoffee -> orderCoffee.getCoffee())
                .collect(Collectors.toList());
        return coffees;
    }

    public Page<Coffee> findCoffees(int page, int size) {
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        return coffeeRepository.findAll(PageRequest.of(page, size, Sort.by("coffeeId").descending()));
    }

    public void deleteCoffee(long coffeeId) {
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Coffee coffee = findVerifiedCoffee(coffeeId);
        coffeeRepository.delete(coffee);
    }

    public Coffee findVerifiedCoffee(long coffeeId) {
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId);
        Coffee findCoffee = optionalCoffee.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));

        return findCoffee;
    }

    private void verifyExistCoffee(String coffeeCode) {
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Optional<Coffee> coffee = coffeeRepository.findByCoffeeCode(coffeeCode);
        if (coffee.isPresent())
            throw new BusinessLogicException(ExceptionCode.COFFEE_CODE_EXISTS);
    }

    private Coffee findVerifiedCoffeeByQuery(long coffeeId) {
        //  throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId);
        Coffee findCoffee =
                optionalCoffee.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));
        return findCoffee;
    }
}
