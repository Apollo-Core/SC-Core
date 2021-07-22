package at.uibk.dps.sc.core.interpreter;

import static org.junit.jupiter.api.Assertions.*;
import at.uibk.dps.ee.enactables.local.container.ContainerFunction;
import at.uibk.dps.ee.enactables.local.container.FunctionFactoryLocal;
import at.uibk.dps.ee.enactables.local.demo.FunctionFactoryDemo;
import at.uibk.dps.ee.enactables.serverless.FunctionFactoryServerless;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUser;
import at.uibk.dps.ee.model.properties.PropertyServiceMapping;
import at.uibk.dps.ee.model.properties.PropertyServiceMapping.EnactmentMode;
import at.uibk.dps.ee.model.properties.PropertyServiceResource;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ScheduleInterpreterUserSingleTest {

  @Test
  public void test() {
    Task task = PropertyServiceFunctionUser.createUserTask("task", "Addition");
    Resource local = PropertyServiceResource.createResource("res");
    Mapping<Task, Resource> localMapping = PropertyServiceMapping.createMapping(task, local,
        EnactmentMode.Local, ConstantsEEModel.implIdLocalNative);
    Set<Mapping<Task, Resource>> localSchedule = new HashSet<>();
    localSchedule.add(localMapping);
    ContainerFunction functionMockLockal = mock(ContainerFunction.class);
    FunctionFactoryLocal factoryMock = mock(FunctionFactoryLocal.class);
    FunctionFactoryServerless mockFacSl = mock(FunctionFactoryServerless.class);
    FunctionFactoryDemo mockDemo = mock(FunctionFactoryDemo.class);
    when(factoryMock.makeFunction(localMapping)).thenReturn(functionMockLockal);
    ScheduleInterpreterUserSingle tested =
        new ScheduleInterpreterUserSingle(factoryMock, mockFacSl, mockDemo);
    assertEquals(functionMockLockal, tested.interpretSchedule(task, localSchedule));
  }

  @Test
  public void testCheckSchedule() {
    assertThrows(IllegalArgumentException.class, () -> {
      FunctionFactoryLocal mockFactory = mock(FunctionFactoryLocal.class);
      FunctionFactoryServerless mockFacSl = mock(FunctionFactoryServerless.class);
      FunctionFactoryDemo mockDemo = mock(FunctionFactoryDemo.class);
      ScheduleInterpreterUserSingle tested =
          new ScheduleInterpreterUserSingle(mockFactory, mockFacSl, mockDemo);
      Task task = new Task("task");
      Resource res = new Resource("res");
      Resource res2 = new Resource("res2");
      Mapping<Task, Resource> m1 = new Mapping<>("m1", task, res);
      Mapping<Task, Resource> m2 = new Mapping<>("m2", task, res2);
      Set<Mapping<Task, Resource>> schedule = new HashSet<>();
      schedule.add(m1);
      schedule.add(m2);
      tested.checkSchedule(task, schedule);
    });
  }
}
