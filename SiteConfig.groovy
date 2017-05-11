import com.sysgears.theme.ResourceMapper
import com.sysgears.theme.deploy.GHPagesDeployer
import com.sysgears.theme.taglib.ThemeTagLib

// This setting defines the character encoding of html pages,
// and therefore should match the character encoding of the site files on a filesystem
html_encoding = 'utf-8' // it is passed to the mata charset attribute of the default page layout

// Resource mapper and tag libs.
resource_mapper = new ResourceMapper(site).map
tag_libs = [ThemeTagLib]

datetime_format = 'dd.MM.yyyy HH:mm'

features {
    highlight = 'pygments' // 'none', 'pygments'
    markdown = 'txtmark'   // 'txtmark', 'pegdown'
    asciidoc {
        opts = ['source-highlighter': 'coderay',
                'icons': 'font']
    }
}

environments {
    dev {
        log.info 'Development environment is used'
        url = "http://localhost:${jetty_port}"
        show_unpublished = true
        features {
            compass = 'none'
        }
    }
    prod {
        log.info 'Production environment is used'
        url = 'https://www.etcdevteam.com'
        show_unpublished = false
        features {
            compass = 'none'
            minify_xml = false
            minify_html = false
            minify_js = false
            minify_css = false
        }
    }
    cmd {
        features {
            compass = 'none'
            highlight = 'none'
        }
    }
}

python {
    //interpreter = 'jython' // 'auto', 'python', 'jython'
    //cmd_candidates = ['python2', 'python', 'python2.7']
    //setup_tools = '2.1'
}

ruby {
    //interpreter = 'jruby'   // 'auto', 'ruby', 'jruby'
    //cmd_candidates = ['ruby', 'ruby1.8.7', 'ruby1.9.3', 'user.home/.rvm/bin/ruby']
    ruby_gems = '2.0.17'
}

// Custom commands-line commands.
commands = [
/*
 * Creates new page.
 *
 * location - relative path to the new page, should start with the /, i.e. /pages/index.html.
 * pageTitle - new page title
 */
'create-page': { String location, String pageTitle ->
        file = new File(content_dir, location)
        file.parentFile.mkdirs()
        file.exists() || file.write("""---
layout: default
title: "${pageTitle}"
published: true
---
""")}
]

etcdev {
    etcaddr = '0x0e7c045110b8dbf29765047380898919c5cb56f4'
    btcaddr = '13XN65AXx4n1E5Q8RawoePstL2Jn8dHgfv'
}