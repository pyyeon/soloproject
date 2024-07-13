package com.springboot.order.entity;

import com.springboot.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    //mappedby는 변수명으로 적음
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    //오더가 등록될때 영속성 전이
    private List<OrderCoffee> orderCoffees = new ArrayList<>();


    public void setOrderCoffee(OrderCoffee orderCoffee) {
        orderCoffees.add(orderCoffee);
        if (orderCoffee.getOrder() != this) {
            orderCoffee.setOrder(this);
        }
    }

    public void addMember(Member member) {
        //member의 입장에서도 연결이 필요함
        //member가 가지고 있는 orders(List)에
        //나자신 Member(this)를 추가함
        this.member = member;
        if (!member.getOrders().contains(this)) {
            member.addOrder(this);
        }

    }

    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}
