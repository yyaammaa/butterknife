package butterknife.internal;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static butterknife.internal.ProcessorTestUtilities.butterknifeProcessors;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.truth0.Truth.ASSERT;

public class OnClickTest {
  @Test public void onClickInjection() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import butterknife.OnClick;",
        "public class Test extends Activity {",
        "  @OnClick(1) void doStuff() {}",
        "}"));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/Test$$ViewInjector",
        Joiner.on('\n').join(
            "package test;",
            "import android.view.View;",
            "import butterknife.ButterKnife.Finder;",
            "public class Test$$ViewInjector {",
            "  public static void inject(Finder finder, final test.Test target, Object source) {",
            "    View view;",
            "    view = finder.findById(source, 1);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '1' for method 'doStuff' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.doStuff();",
            "      }",
            "    });",
            "  }",
            "  public static void reset(test.Test target) {",
            "  }",
            "}"
        ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void methodVisibility() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import android.view.View;",
        "import butterknife.OnClick;",
        "public class Test extends Activity {",
        "  @OnClick(1) public void thing1() {}",
        "  @OnClick(2) void thing2() {}",
        "  @OnClick(3) protected void thing3() {}",
        "}"
    ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError();
  }

  @Test public void methodCastsArgument() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import android.view.View;",
        "import android.widget.Button;",
        "import android.widget.TextView;",
        "import butterknife.OnClick;",
        "public class Test extends Activity {",
        "  @OnClick(0) void click0() {}",
        "  @OnClick(1) void click1(View view) {}",
        "  @OnClick(2) void click2(TextView view) {}",
        "  @OnClick(3) void click3(Button button) {}",
        "}"
    ));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/Test$$ViewInjector",
        Joiner.on('\n').join(
            "package test;",
            "import android.view.View;",
            "import butterknife.ButterKnife.Finder;",
            "public class Test$$ViewInjector {",
            "  public static void inject(Finder finder, final test.Test target, Object source) {",
            "    View view;",
            "    view = finder.findById(source, 0);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '0' for method 'click0' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click0();",
            "      }",
            "    });",
            "    view = finder.findById(source, 1);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '1' for method 'click1' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click1(p0);",
            "      }",
            "    });",
            "    view = finder.findById(source, 2);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '2' for method 'click2' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click2((android.widget.TextView) p0);",
            "      }",
            "    });",
            "    view = finder.findById(source, 3);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '3' for method 'click3' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click3((android.widget.Button) p0);",
            "      }",
            "    });",
            "  }",
            "  public static void reset(test.Test target) {",
            "  }",
            "}"
        ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void methodWithMultipleIds() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import android.view.View;",
        "import butterknife.OnClick;",
        "public class Test extends Activity {",
        "  @OnClick({1, 2, 3}) void click() {}",
        "}"
    ));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/Test$$ViewInjector",
        Joiner.on('\n').join(
            "package test;",
            "import android.view.View;",
            "import butterknife.ButterKnife.Finder;",
            "public class Test$$ViewInjector {",
            "  public static void inject(Finder finder, final test.Test target, Object source) {",
            "    View view;",
            "    view = finder.findById(source, 1);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '1' for method 'click' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click();",
            "      }",
            "    });",
            "    view = finder.findById(source, 2);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '2' for method 'click' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click();",
            "      }",
            "    });",
            "    view = finder.findById(source, 3);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '3' for method 'click' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.click();",
            "      }",
            "    });",
            "  }",
            "  public static void reset(test.Test target) {",
            "  }",
            "}"
        ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void optional() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import butterknife.OnClick;",
        "import butterknife.Optional;",
        "public class Test extends Activity {",
        "  @Optional @OnClick(1) void doStuff() {}",
        "}"));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/Test$$ViewInjector",
        Joiner.on('\n').join(
            "package test;",
            "import android.view.View;",
            "import butterknife.ButterKnife.Finder;",
            "public class Test$$ViewInjector {",
            "  public static void inject(Finder finder, final test.Test target, Object source) {",
            "    View view;",
            "    view = finder.findById(source, 1);",
            "    if (view != null) {",
            "      view.setOnClickListener(new android.view.View.OnClickListener() {",
            "        @Override public void onClick(android.view.View p0) {",
            "          target.doStuff();",
            "        }",
            "      });",
            "    }",
            "  }",
            "  public static void reset(test.Test target) {",
            "  }",
            "}"
        ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }


  @Test public void optionalAndRequiredSkipsNullCheck() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import android.app.Activity;",
        "import android.view.View;",
        "import butterknife.InjectView;",
        "import butterknife.OnClick;",
        "import butterknife.Optional;",
        "public class Test extends Activity {",
        "  @InjectView(1) View view;",
        "  @Optional @OnClick(1) void doStuff() {}",
        "}"));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/Test$$ViewInjector",
        Joiner.on('\n').join(
            "package test;",
            "import android.view.View;",
            "import butterknife.ButterKnife.Finder;",
            "public class Test$$ViewInjector {",
            "  public static void inject(Finder finder, final test.Test target, Object source) {",
            "    View view;",
            "    view = finder.findById(source, 1);",
            "    if (view == null) {",
            "      throw new IllegalStateException(\"Required view with id '1' for field 'view' was not found. If this view is optional add '@Optional' annotation.\");",
            "    }",
            "    target.view = view;",
            "    view.setOnClickListener(new android.view.View.OnClickListener() {",
            "      @Override public void onClick(android.view.View p0) {",
            "        target.doStuff();",
            "      }",
            "    });",
            "  }",
            "  public static void reset(test.Test target) {",
            "    target.view = null;",
            "  }",
            "}"
        ));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void failsIfHasReturnType() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import android.app.Activity;",
            "import butterknife.OnClick;",
            "import java.lang.String;",
            "public class Test extends Activity {",
            "  @OnClick(1)",
            "  public String doStuff() {",
            "  }",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(
            String.format("@OnClick methods must have a 'void' return type. (%s)",
                "test.Test.doStuff"))
        .in(source).onLine(7);
  }

  @Test public void failsIfPrivate() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import android.app.Activity;",
            "import butterknife.OnClick;",
            "public class Test extends Activity {",
            "  @OnClick(1)",
            "  private void doStuff() {",
            "  }",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(
            String.format("@OnClick methods must not be private or static. (%s)",
                "test.Test.doStuff"))
        .in(source).onLine(6);
  }

  @Test public void failsIfStatic() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import android.app.Activity;",
            "import butterknife.OnClick;",
            "public class Test extends Activity {",
            "  @OnClick(1)",
            "  public static void doStuff() {",
            "  }",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(
            String.format("@OnClick methods must not be private or static. (%s)",
                "test.Test.doStuff"))
        .in(source).onLine(6);
  }

  @Test public void failsIfParameterNotView() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import android.app.Activity;",
            "import butterknife.OnClick;",
            "public class Test extends Activity {",
            "  @OnClick(1)",
            "  public void doStuff(String thing) {",
            "  }",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(Joiner.on('\n').join(
            "Unable to match @OnClick method arguments. (test.Test.doStuff)",
            "",
            "  Parameter #1: java.lang.String",
            "    did not match any listener parameters",
            "",
            "Methods may have up to 1 parameter(s):",
            "",
            "  android.view.View",
            "",
            "These may be listed in any order but will be searched for from top to bottom."))
        .in(source).onLine(6);
  }

  @Test public void failsIfMoreThanOneParameter() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import android.app.Activity;",
            "import android.view.View;",
            "import butterknife.OnClick;",
            "public class Test extends Activity {",
            "  @OnClick(1)",
            "  public void doStuff(View thing, View otherThing) {",
            "  }",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(
            String.format("@OnClick methods can have at most 1 parameter(s). (%s)",
                "test.Test.doStuff"))
        .in(source).onLine(7);
  }

  @Test public void failsIfInInterface() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import butterknife.OnClick;",
            "public interface Test {",
            "  @OnClick(1)",
            "  void doStuff();",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(
            String.format("@OnClick methods may only be contained in classes. (%s)",
                "test.Test.doStuff"))
        .in(source).onLine(3);
  }

  @Test public void failsIfHasDuplicateIds() {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test",
        Joiner.on('\n').join(
            "package test;",
            "import android.app.Activity;",
            "import butterknife.OnClick;",
            "public class Test extends Activity {",
            "  @OnClick({1, 2, 3, 1})",
            "  void doStuff() {",
            "  }",
            "}"));

    ASSERT.about(javaSource()).that(source)
        .processedWith(butterknifeProcessors())
        .failsToCompile()
        .withErrorContaining(
            String.format("@OnClick annotation for method contains duplicate ID %d. (%s)",
                1, "test.Test.doStuff"))
        .in(source).onLine(6);
  }
}
