package cv.pn.sibs.utilities;

import lombok.Data;

@Data
public class Constants {

    public enum OrderStatus {

        W("Waiting Stock"), C("Concluded");

        private final String orderStatus;
        OrderStatus(String orderStatus) {

            this.orderStatus = orderStatus;
        }

        public String getType() {

            return this.orderStatus;
        }
    }

    public enum ItemStatus {

        C("Concluded"), U("Unavailable");

        private final String itemStatus;
        ItemStatus(String itemStatus) {

            this.itemStatus = itemStatus;
        }

        public String getType() {

            return this.itemStatus;
        }
    }
}
