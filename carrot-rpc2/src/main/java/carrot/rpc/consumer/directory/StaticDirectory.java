package carrot.rpc.consumer.directory;

import carrot.rpc.common.Invoker;

import java.util.List;

public class StaticDirectory implements Directory {

    private List<Invoker> invokers;

    public void setInvokers(List<Invoker> invokers) {
        this.invokers = invokers;
    }

    public List<Invoker> list() {
        return this.invokers;
    }
}
