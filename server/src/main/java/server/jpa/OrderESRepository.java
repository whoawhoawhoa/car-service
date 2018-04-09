package server.jpa;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Date;
import java.util.List;

public interface OrderESRepository extends ElasticsearchRepository<OrderES, Long>, PagingAndSortingRepository<OrderES, Long> {
    OrderES findOrderESById(long id);
    List<OrderES> findOrderESByServiceType(String serviceType);
    List<OrderES> findOrderESByCar_CarType_CarType(String carType);
    List<OrderES> findOrderESByClient_Login(String clientLogin);
    List<OrderES> findOrderESByWorker_Login(String workerLogin);
    List<OrderES> findOrderESByTotalPriceBetween(long price1, long price2);
    List<OrderES> findOrderESByTotalPriceLessThan(long price);
    List<OrderES> findOrderESByTotalPriceGreaterThan(long price);
    List<OrderES> findOrderESByOrderDateBefore(Date date);
    List<OrderES> findOrderESByOrderDateAfter(Date date);
    void deleteByIdGreaterThan(long id);
    List<OrderES> findOrderESByClient_LoginContainingOrWorker_CityContainingOrWorker_LoginContainingOrClient_CityContainingOrServiceTypeContainingOrCar_CarType_CarType(String part1, String part2, String part3, String part4, String part5, String part6);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetween(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndWorker_Login(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String workerLogin);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_Login(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String clientLogin);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_LoginAndWorker_Login(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String clientLogin, String workerLogin);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndWorker_Login(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType, String workerLogin);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndClient_Login(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType, String clientLogin);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndClient_LoginAndWorker_Login(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType, String clientLogin, String workerLogin);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String serviceType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndWorker_LoginAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String workerLogin, String serviceType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_LoginAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String clientLogin, String serviceType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndClient_LoginAndWorker_LoginAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String clientLogin, String workerLogin, String serviceType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType, String serviceType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndWorker_LoginAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType, String workerLogin, String serviceType);

    List<OrderES> findOrderESByTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetweenAndCar_CarType_CarTypeAndClient_LoginAndServiceType(
            long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark, String carType, String clientLogin, String serviceType);

    List<OrderES> findOrderESByServiceTypeAndCar_CarType_CarTypeAndClient_LoginAndWorker_LoginAndTotalPriceBetweenAndOrderDateBetweenAndClientMarkBetweenAndWorkerMarkBetween(
            String serviceType,String carType,String clientLogin,String workerLogin, long price1, long price2, Date fromDate, Date toDate, long fromClientMark, long toClientMark, long fromWorkerMark, long toWorkerMark);


    List<OrderES> findOrderESByClient_LoginAndServiceTypeAndTotalPriceBetweenAndOrderDateBetween(
            String clientLogin, String serviceType, long price1, long price2, Date fromDate, Date toDate);

    List<OrderES> findOrderESByClient_LoginAndTotalPriceBetweenAndOrderDateBetween(
            String clientLogin, long price1, long price2, Date fromDate, Date toDate);

    List<OrderES> findOrderESByWorker_LoginAndServiceTypeAndTotalPriceBetweenAndOrderDateBetween(
            String workerLogin, String serviceType, long price1, long price2, Date fromDate, Date toDate);

    List<OrderES> findOrderESByWorker_LoginAndTotalPriceBetweenAndOrderDateBetween(
            String workerLogin, long price1, long price2, Date fromDate, Date toDate);

    List<OrderES> findOrderESByCar_Id(long id);

    List<OrderES> findOrderESByClientMark(long clientMark);


}
