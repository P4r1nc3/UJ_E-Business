/// <reference types="cypress" />

describe('Apple.com Tests', () => {

  beforeEach(() => {
    cy.visit('https://www.apple.com')
  })

  it('Successfully loads the home page', () => {
    cy.url().should('include', 'apple.com')
  })

  it('Has a working navigation bar with specific links', () => {
    cy.get('nav').should('exist')
    cy.get('nav').contains('Store').should('exist')
    cy.get('nav').contains('Mac').should('exist')
    cy.get('nav').contains('iPad').should('exist')
    cy.get('nav').contains('iPhone').should('exist')
    cy.get('nav').contains('Watch').should('exist')
    cy.get('nav').contains('Vision').should('exist')
    cy.get('nav').contains('AirPods').should('exist')
    cy.get('nav').contains('TV & Home').should('exist')
    cy.get('nav').contains('Entertainment').should('exist')
    cy.get('nav').contains('Accessories').should('exist')
    cy.get('nav').contains('Support').should('exist')
  })

  it('Footer with specific sections', () => {
    cy.get('footer').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('Shop and Learn').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('Apple Wallet').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('Account').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('Entertainment').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('Apple Store').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('For Business').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('For Education').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('For Healthcare').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('For Government').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('Apple Values').should('exist')
    cy.get('.ac-gf-directory-column-section-title').contains('About Apple').should('exist')
  });

  it('Shop and Learn with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Store').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Mac').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('iPad').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('iPhone').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Watch').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Vision').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('AirPods').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('TV & Home').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('AirTag').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Accessories').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Gift Cards').should('exist');
  });

  it('Apple Wallet with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Wallet').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Card').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Pay').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Cash').should('exist');
  });

  it('Account with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Manage Your Apple ID').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Store Account').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('iCloud.com').should('exist');
  });

  it('Entertainment with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Apple One').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple TV+').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Music').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Arcade').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Fitness+').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple News+').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Podcasts').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Books').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('App Store').should('exist');
  });

  it('Apple Store with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Find a Store').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Genius Bar').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Today at Apple').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Camp').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Store App').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Certified Refurbished').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Trade In').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Financing').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Carrier Deals at Apple').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Order Status').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Shopping Help').should('exist');
  });

  it('For Business with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Apple and Business').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Shop for Business').should('exist');
  });

  it('For Education with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Apple and Education').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Shop for K-12').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Shop for College').should('exist');
  });

  it('For Healthcare with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Apple in Healthcare').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Health on Apple Watch').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Health Records on iPhone').should('exist');
  });

  it('For Government with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Shop for Government').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Shop for Veterans and Military').should('exist');
  });

  it('Apple Values with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Accessibility').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Education').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Environment').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Inclusion and Diversity').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Privacy').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Racial Equity and Justice').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Supplier Responsibility').should('exist');
  });

  it('About Apple with specific links', () => {
    cy.get('.ac-gf-directory-column-section-list').contains('Newsroom').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Apple Leadership').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Career Opportunities').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Investors').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Ethics & Compliance').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Events').should('exist');
    cy.get('.ac-gf-directory-column-section-list').contains('Contact Apple').should('exist');
  });

  it('Search for iPhone', () => {
    cy.get('nav').find('#globalnav-menubutton-link-search').click()
    cy.get('.globalnav-searchfield-input').type('iPhone 15{enter}')
    cy.contains('a', 'iPhone 15 Pro and iPhone 15 Pro Max').should('exist')
    cy.contains('a', 'iPhone 15 and iPhone 15 Plus').should('exist')
  });

  it('Search for Mac', () => {
    cy.get('nav').find('#globalnav-menubutton-link-search').click()
    cy.get('.globalnav-searchfield-input').type('Mac{enter}')
    cy.contains('a', 'Mac').should('exist')
    cy.contains('a', 'MacBook Pro').should('exist')
    cy.contains('a', 'MacBook Air').should('exist')
  });

  it('Search for AirPods', () => {
    cy.get('nav').find('#globalnav-menubutton-link-search').click()
    cy.get('.globalnav-searchfield-input').type('AirPods{enter}')
    cy.contains('a', 'AirPods').should('exist')
    cy.contains('a', 'AirPods (3rd generation)').should('exist')
    cy.contains('a', 'AirPods Pro (2nd generation)').should('exist')
  });

  it('Search for Watch', () => {
    cy.get('nav').find('#globalnav-menubutton-link-search').click()
    cy.get('.globalnav-searchfield-input').type('Watch{enter}')
    cy.contains('a', 'Apple Watch').should('exist')
    cy.contains('a', 'Apple Watch Series 9').should('exist')
    cy.contains('a', 'Apple Watch Ultra 2').should('exist')
  });

  it('Search for iPad', () => {
    cy.get('nav').find('#globalnav-menubutton-link-search').click()
    cy.get('.globalnav-searchfield-input').type('iPad{enter}')
    cy.contains('a', 'Explore iPad').should('exist')
    cy.contains('a', 'iPad').should('exist')
    cy.contains('a', 'iPad Pro').should('exist')
  });

  it('Apple logo returns to home', () => {
    cy.get('.globalnav').find('a').first().click()
    cy.url().should('eq', 'https://www.apple.com/')
  })
});
