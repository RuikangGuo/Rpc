package carrot.rpc.consumer.directory;

import carrot.rpc.common.Invoker;

import java.util.List;

public interface Directory {
    List<Invoker> list();
}
