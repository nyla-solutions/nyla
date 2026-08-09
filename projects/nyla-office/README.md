# NYLA Office

Utilities for **Excel** sheet access (JExcelAPI / JXL), **JFreeChart** charts, and **PDF** generation via Apache FOP.

Maven artifact: `com.github.nyla-solutions:nyla.solutions.office` (see `build.gradle` for the current version).

Depends on [NYLA Core](../core) for I/O and debugging helpers.

Main packages:

| Package | Purpose |
|---------|---------|
| `nyla.solutions.office.msoffice.excel` | `ExcelSheet` abstraction |
| `nyla.solutions.office.msoffice.excel.jxl` | JXL-backed `JxlSheet` |
| `nyla.solutions.office.chart` | Chart types and `JFreeChartFacade` |
| `nyla.solutions.office.fop` | PDF generation from XSL-FO |

---

## Excel (`ExcelSheet` / `JxlSheet`)

Open a workbook with JXL and wrap a sheet with `JxlSheet`:

```java
import jxl.Workbook;
import nyla.solutions.office.msoffice.excel.ExcelSheet;
import nyla.solutions.office.msoffice.excel.jxl.JxlSheet;

import java.io.File;

File file = new File("src/resources/excel/input.xls");
Workbook workbook = Workbook.getWorkbook(file);
ExcelSheet sheet = new JxlSheet(workbook.getSheet(0));

for (String[] row : sheet.getRows()) {
    // process row
}

workbook.close();
```

---

## JFreeChart (`JFreeChartFacade`)

### Pie chart

```java
import nyla.solutions.core.io.IO;
import nyla.solutions.office.chart.Chart;
import nyla.solutions.office.chart.JFreeChartFacade;

import java.io.File;

Chart chart = new JFreeChartFacade();
chart.setCategoryLabel(categoryLabel);
chart.setHeight(height);
chart.setName(name);
chart.plotValue(100.0, "Group A (66%)", "Usage");
chart.plotValue(50.0, "Group B (34%)", "Usage");
chart.setTypeName(Chart.PNG_TYPE_NAME);
chart.setGraphType(Chart.PIE_GRAPH_TYPE);

IO.writeFile(new File(filePath), chart.getBytes());
```

### Area chart (example: JVM memory over weekdays)

```java
import nyla.solutions.core.io.IO;
import nyla.solutions.office.chart.Chart;
import nyla.solutions.office.chart.JFreeChartFacade;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

Chart chart = new JFreeChartFacade();
chart.setLegend(legend);
chart.setCategoryLabel(categoryLabel);
chart.setHeight(height);
chart.setName(name);
chart.setWidth(width);
chart.setTitle(title);

String label = "JVM";
Calendar cal = Calendar.getInstance();
cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
cal.add(Calendar.DATE, -5);

chart.plotValue(2, label, cal.getTime());
cal.add(Calendar.DATE, 1);
chart.plotValue(5, label, cal.getTime());
cal.add(Calendar.DATE, 1);
chart.plotValue(6, label, cal.getTime());
cal.add(Calendar.DATE, 1);
chart.plotValue(7, label, cal.getTime());
cal.add(Calendar.DATE, 1);
chart.plotValue(1, label, cal.getTime());

chart.setTypeName(Chart.PNG_TYPE_NAME);
chart.setSeriesColors(Arrays.asList(Color.RED));
chart.setBackgroundColor(Color.WHITE);
chart.setValueLabel("GB(s)");
chart.setGraphType(Chart.AREA_GRAPH_TYPE);

IO.writeFile(new File(filePath), chart.getBytes());
```

---

## FOP PDF

Generate PDF from XSL-FO markup using [`FOP`](src/main/java/nyla/solutions/office/fop/FOP.java):

```java
import nyla.solutions.core.io.IO;
import nyla.solutions.office.fop.FOP;

import java.io.File;

String fo = IO.reader().readClassPath("pdf/example.fop");
File file = new File("src/test/resources/pdf/test.pdf");
file.delete();

FOP.writePDF(fo, file);
```

Minimal FO document:

```xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="A4">
      <fo:region-body />
    </fo:simple-page-master>
  </fo:layout-master-set>
  <fo:page-sequence master-reference="A4">
    <fo:flow flow-name="xsl-region-body">
      <fo:block>Hello World</fo:block>
    </fo:flow>
  </fo:page-sequence>
</fo:root>
```

Embed an image:

```xml
<fo:block>
  <fo:external-graphic src="../graphics/xml_feather_transparent.gif"/>
</fo:block>
```

Further XSL-FO reference: [W3Schools XSL-FO tutorial](https://www.w3schools.com/xml/xslfo_intro.asp).
