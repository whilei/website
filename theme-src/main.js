import $ from 'jquery'
import 'animate.css'
import 'bootstrap/less/bootstrap.less'
import 'font-awesome/css/font-awesome.css'
import 'jquery-fancybox/source/scss/jquery.fancybox.scss'
import 'jquery-fancybox/source/js/jquery.fancybox'

import './lib/inviewport-1.3.2'
import './lib/jquery.isotope-v1'
import './lib/jquery.localscroll-1.2.7-min'
import './lib/jquery.mixitup.min'
import './lib/jquery.parallax-1.1.3'
import './lib/jquery.scrollTo-1.4.6-min'
import './lib/owl.carousel'

import './styles/pace-loading-bar.css'
import './styles/animate.caliber.css'
import 'mdi/scss/materialdesignicons.scss'
// import './styles/style.css'
import './styles/style.etc.scss'
import './styles/etc.scss'
import './styles/texts.scss'

import './pace'
import './caliber'

import QRCode from 'qrjs'

$(document).ready(() => {
    $('.donation').each( (i, d) => {
        const el = $(d);
        const addr = el.data('address');
        console.log('d', addr);
        const qr = QRCode.generateHTML(addr);
        $('p.qr', el).append(qr);
    })
});