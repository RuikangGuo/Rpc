package carrot.rpc.test;

public class TestServiceImpl implements TestService {
    public String hello(String name) {
        return "hello "+ name;
    }
}
