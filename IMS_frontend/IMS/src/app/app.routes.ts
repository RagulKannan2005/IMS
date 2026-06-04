import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Maindashboard } from './features/dashboard/maindashboard/maindashboard';
import { Register } from './features/auth/register/register';

export const routes: Routes = [
    {
        path:'login',
        component:Login
    },
    {
        path:'dashboard',
        component:Maindashboard
    },
    {
        path:'register',
        component:Register
    },
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    }
];
