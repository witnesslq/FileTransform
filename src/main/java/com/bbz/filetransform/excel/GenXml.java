package com.bbz.filetransform.excel;


import com.bbz.filetransform.PathCfg;
import com.bbz.filetransform.util.D;
import com.bbz.filetransform.util.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 根据excel文件构建xml文档
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-5
 * Time: 下午4:32
 */
class GenXml extends AbstractGen{

    private final String className;
    private final String packageName;

    public GenXml( String[] path, Sheet sheet ){
        super(new FieldElimentManager( sheet ).getFields(),sheet);
        className = path[1];
        packageName = path[0];
    }

    void generate(){

        StringBuilder sb = new StringBuilder( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" );
        sb.append( "<" ).append( className ).append( "s" ).append( ">" );
        for( Row row : sheet ) {
            if( row.getRowNum() < D.EXCEL_HEAD_COUNT ) {
                continue;
            }

            if( row.getCell( 0 ) == null ) {
                System.out.println( "以下空白，停止处理" );
                break;
            }

            sb.append( "<" ).append( className ).append( ">" ).
                    append( genContent( row ) ).append( "</" ).append( className ).append( ">" );
        }
        sb.append( "</" ).append( className ).append( "s" ).append( ">" );
        //System.out.println( sb.toString() );

        String path = PathCfg.EXCEL_OUTPUT_XML_PATH + packageName + "/" + Util.firstToLowCase( className ) + ".xml";
    }

    public String genContent( Row row ){
        //printRow( row );不出错，无需打印
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for( FieldElement element : fields ) {
            sb.append( "\"" ).append( element.name ).append( "\":" );

            Cell cell = row.getCell( i++ );


            //System.out.println( row.getCell(i++) );
            sb.append( "\"" ).append( getCellStr( cell, fieldIsIntOrLong(element) ) );
            sb.append( "\"," );

        }
        if( sb.length() > 1 ) {
            sb.deleteCharAt( sb.length() - 1 );
        }
        return sb.toString();
    }

}
