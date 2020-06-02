package com.step.stupid.social.network.factory;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("andreyOperationSystem")
@MSDOSEverywhere
public class OperationSystem {

    @MSDOSEverywhere(operationSystem = "Windows")
    private String operationSystem;

    @PostConstruct
    public void init() {
        System.out.println("Post Construct is called");
    }

    public OperationSystem() {
    }

    public String getOperationSystem() {
        return operationSystem;
    }

    public void setOperationSystem(String operationSystem) {
        this.operationSystem = operationSystem;
    }
}
