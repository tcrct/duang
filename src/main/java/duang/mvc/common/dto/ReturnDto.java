package duang.mvc.common.dto;

public class ReturnDto<T> implements java.io.Serializable {

    /**头部信息*/
    private HeadDto headDto;
    /**主体内容*/
    private T data;

    public ReturnDto() {
    }

    public ReturnDto(HeadDto headDto) {
        this.headDto = headDto;
    }

    public ReturnDto(HeadDto headDto, T data) {
        this.headDto = headDto;
        this.data = data;
    }

    public HeadDto getHeadDto() {
        return headDto;
    }

    public void setHeadDto(HeadDto headDto) {
        this.headDto = headDto;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnDto{" +
                "headDto=" + headDto +
                ", data=" + data +
                '}';
    }
}
